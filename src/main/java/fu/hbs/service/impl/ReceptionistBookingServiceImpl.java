package fu.hbs.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import fu.hbs.dto.HotelBookingDTO.*;
import fu.hbs.entities.*;
import fu.hbs.exceptionHandler.CheckoutException;
import fu.hbs.exceptionHandler.NotEnoughRoomAvalaibleException;
import fu.hbs.repository.*;
import fu.hbs.utils.BookingUtil;
import fu.hbs.utils.RandomKey;
import fu.hbs.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;

import fu.hbs.service.dao.ReceptionistBookingService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ReceptionistBookingServiceImpl implements ReceptionistBookingService {
    @Autowired
    private HotelBookingRepository bookingRepository;
    @Autowired
    private RoomStatusRepository roomStatusRepository; // Autowire RoomStatusRepository
    @Autowired
    private BookingRoomDetailsRepository bookingRoomDetailsRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelBookingServiceRepository hotelBookingServiceRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;
    @Autowired
    private CategoryRoomPriceRepository categoryRoomPriceRepository;

    @Override
    public List<HotelBooking> findAll() {
        return bookingRepository.findAll();
    }


    @Override
    public HotelBooking findById(Long id) {
        Optional<HotelBooking> result = bookingRepository.findById(id);
        HotelBooking booking = null;
        if (result.isPresent()) {
            booking = result.get();
        } else {
            throw new RuntimeException("Không tìm thấy đặt phòng có id - " + id);
        }
        return booking;
    }

    @Override
    public void save(HotelBooking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<HotelBooking> findAllWithStatusOne() {
        return bookingRepository.findByStatusId(1L);
    }

    @Override
    public List<HotelBooking> findAllWithStatusTwo() {
        return bookingRepository.findByStatusId(2L);
    }

    @Override
    @Transactional
    public Long createHotelBookingByReceptionist(CreateHotelBookingDTO bookingRequest) {
        // Create basic object booking and save to get the id
        HotelBooking hotelBooking = new HotelBooking();
        hotelBooking.setUserId(null);
        hotelBooking.setStatusId(1L);
        hotelBooking.setName(bookingRequest.getName());
        hotelBooking.setEmail(bookingRequest.getEmail());
        hotelBooking.setAddress(bookingRequest.getAddress());
        hotelBooking.setPhone(bookingRequest.getPhone());
        LocalDate checkInLocalDate = bookingRequest.getCheckIn();
        LocalDate checkOutLocalDate = bookingRequest.getCheckOut();

        LocalDateTime checkInWithSpecificTime = LocalDateTime.of(checkInLocalDate.getYear(), checkInLocalDate.getMonth(), checkInLocalDate.getDayOfMonth(), 12, 30, 0); // Year, Month, Day, Hour, Minute, Second
        LocalDateTime checkoutWithSpecificTime = LocalDateTime.of(checkOutLocalDate.getYear(), checkOutLocalDate.getMonth(), checkOutLocalDate.getDayOfMonth(), 12, 30, 0); // Year, Month, Day, Hour, Minute, Second
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

        Instant checkOut = checkoutWithSpecificTime.atZone(zoneId).toInstant();
        Instant checkIn = checkInWithSpecificTime.atZone(zoneId).toInstant();

        hotelBooking.setCheckIn(checkIn);
        hotelBooking.setCheckOut(checkOut);
        boolean validTimeForBooking = BookingValidator.isValidTimeForBooking(checkIn, checkOut);
        if (!validTimeForBooking) {
            throw new RuntimeException("Thời gian đặt phòng không hợp lệ!");
        }
        // Calculate total room number
        Optional<Integer> totalRoomNumberOp = bookingRequest.getBookingDetails().stream().map(CreateHotelBookingDetailDTO::getRoomNumber).reduce(Integer::sum);
        totalRoomNumberOp.ifPresent(hotelBooking::setTotalRoom);

        // Save to get id before create Booking Details
        bookingRepository.save(hotelBooking);

        // Base on request and then create booking detail
        List<BookingRoomDetails> bookingDetails = createBookingDetails(bookingRequest, checkIn, checkOut, hotelBooking);
        bookingRoomDetailsRepository.saveAll(bookingDetails);
        BigDecimal totalPrice = this.calculateTotalPrice(bookingDetails,checkIn,checkOut);
        totalPrice = totalPrice.add(totalPrice.multiply(BigDecimal.valueOf(0.1))).setScale(0, RoundingMode.HALF_DOWN);
        // Deposit Option Yes/No
        if (bookingRequest.isPayFull()) {
            hotelBooking.setDepositPrice(totalPrice);
        } else {
            hotelBooking.setDepositPrice(totalPrice.multiply(BigDecimal.valueOf(0.5)).setScale(0, RoundingMode.HALF_DOWN));
        }

        if (bookingRequest.getPaymentTypeId() != 1L) {
            Transactions transaction = new Transactions();
            transaction.setVnpayTransactionId(RandomKey.generateRandomKey());
            transaction.setStatus("Thành công");
            transaction.setAmount(hotelBooking.getDepositPrice());
            transaction.setCreatedDate(Instant.now());
            transaction.setHotelBookingId(hotelBooking.getHotelBookingId());
            transaction.setContent("Thanh toán đặt phòng");
            hotelBooking.setValidBooking(true);
            transactionsRepository.save(transaction);
        }
        hotelBooking.setTotalPrice(totalPrice);
        return hotelBooking.getHotelBookingId();
    }

    private List<BookingRoomDetails> createBookingDetails(CreateHotelBookingDTO bookingRequest, Instant checkIn, Instant checkOut, HotelBooking hotelBooking) {
        List<BookingRoomDetails> bookingRoomDetails = new ArrayList<>(); // All result of BookingRoomDetails
        Map<Long, List<Room>> availableRoomWithCategoryMap = new HashMap<>(); // Map for save temporary all available room in this creating session

        for (CreateHotelBookingDetailDTO bookingDetailRequest : bookingRequest.getBookingDetails()) {
            // Get available room from DB if we not yet store in map
            List<Room> availableRoomsByCategoryIdAndDateBetween = availableRoomWithCategoryMap.get(bookingDetailRequest.getRoomCategoryId());
            if (availableRoomsByCategoryIdAndDateBetween == null) {
                availableRoomsByCategoryIdAndDateBetween = this.findAvailableRoomsByCategoryIdAndDateBetween(checkIn, checkOut, bookingDetailRequest.getRoomCategoryId());
                availableRoomWithCategoryMap.put(bookingDetailRequest.getRoomCategoryId(), availableRoomsByCategoryIdAndDateBetween);
            }
            // Check if available room number less than booking number
            Integer bookingRoomNumber = bookingDetailRequest.getRoomNumber();
            if (availableRoomsByCategoryIdAndDateBetween.size() < bookingDetailRequest.getRoomNumber()) {
                throw new NotEnoughRoomAvalaibleException("Không còn đủ phòng để đặt!");
            }

            for (int i = 0; i < bookingRoomNumber; i++) {
                Room room = availableRoomsByCategoryIdAndDateBetween.stream().findFirst().get();
                BookingRoomDetails bookingRoomDetail = new BookingRoomDetails();
                bookingRoomDetail.setHotelBookingId(hotelBooking.getHotelBookingId());
                bookingRoomDetail.setRoomCategoryId(bookingDetailRequest.getRoomCategoryId());
                bookingRoomDetail.setRoomId(room.getRoomId());
                bookingRoomDetails.add(bookingRoomDetail);
                // Temporarily remove room from Available Room after create a Booking Detail
                availableRoomsByCategoryIdAndDateBetween.remove(room);
            }
        }
        return bookingRoomDetails;
    }

    private List<Room> findAvailableRoomsByCategoryIdAndDateBetween(Instant start, Instant end, Long roomCategoryId) {
        LocalDateTime startDateTime = LocalDateTime.ofInstant(start, ZoneOffset.UTC);
        LocalDateTime endDateTime = LocalDateTime.ofInstant(end, ZoneOffset.UTC);

        return roomRepository.findAvailableRoomsByCategoryId(roomCategoryId, startDateTime.toLocalDate(), endDateTime.toLocalDate());
    }

    private BigDecimal calculateTotalPrice(List<BookingRoomDetails> bookingRoomDetailsList, Instant checkin, Instant checkout) {
        List<Long> bookRoomCategoryIds = bookingRoomDetailsList.stream().map(BookingRoomDetails::getRoomCategoryId).distinct().collect(Collectors.toList());
        Map<Long, CategoryRoomPrice> priceMap = categoryRoomPriceRepository.findAllById(bookRoomCategoryIds).stream().collect(Collectors.toMap(CategoryRoomPrice::getRoomCategoryId, Function.identity()));
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BookingRoomDetails bookingRoomDetails : bookingRoomDetailsList) {
            categoryRoomPriceRepository.getCategoryId(bookingRoomDetails.getRoomCategoryId());
            BigDecimal priceWithDate = BookingUtil.calculatePriceBetweenDate(checkin, checkout, bookingRoomDetails.getRoomCategoryId(),false);
            totalPrice = totalPrice.add(priceWithDate);
        }
        return totalPrice;
    }

    @Override
    public boolean checkout(SaveCheckoutDTO saveCheckoutDTO) {
        Optional<HotelBooking> hotelBookingOptional = bookingRepository.findById(saveCheckoutDTO.getHotelBookingId());
        if (hotelBookingOptional.isPresent()) {
            // Update hotel booking status
            HotelBooking hotelBooking = hotelBookingOptional.get();

            if (!BookingValidator.isValidToCheckOut(hotelBooking)) {
                throw new CheckoutException("Trạng thái của đặt phòng chưa sẵn sàng cho checkout!");
            }
            Instant exptectedCheckoutDate = hotelBooking.getCheckOut();
            hotelBooking.setStatusId(3L);
            hotelBooking.setCheckOut(Instant.now());

            // Change room status
            List<BookingRoomDetails> hotelBookingDetails = bookingRoomDetailsRepository.getAllByHotelBookingId(hotelBooking.getHotelBookingId());
            changeRoomStatus(hotelBookingDetails);

            List<SaveCheckoutHotelServiceDTO> hotelBookingServices = saveCheckoutDTO.getHotelServices();
            hotelBookingServiceRepository.deleteAllByHotelBookingId(hotelBooking.getHotelBookingId()); // Clean all old used service and use latest ones
            Map<Long, RoomService> allRoomServiceAsMap = BookingUtil.getAllRoomServiceAsMap();

            List<HotelBookingService> hotelBookingServiceList = new ArrayList<>();
            BigDecimal servicePrice = BigDecimal.ZERO;
            servicePrice = calculateServicePrice(hotelBookingServices, allRoomServiceAsMap, servicePrice, hotelBooking, hotelBookingServiceList);

            BigDecimal roomPrice = BigDecimal.ZERO;
            roomPrice = calculateRoomPrice(hotelBookingDetails, roomPrice, hotelBooking.getCheckIn(),exptectedCheckoutDate);

            updateTotalPriceOfBooking(servicePrice, roomPrice, hotelBooking);

            if (!(saveCheckoutDTO.getPaymentTypeId() == 1)) {
                Transactions transactions = makeTransaction(hotelBooking.getTotalPrice(), hotelBooking, hotelBooking.getDepositPrice(),saveCheckoutDTO.getPaymentTypeId());

                transactionsRepository.save(transactions);
            }
            hotelBookingServiceRepository.saveAll(hotelBookingServiceList);
            bookingRepository.save(hotelBooking);
            return true;
        }
        return false;
    }

    private static void updateTotalPriceOfBooking(BigDecimal servicePrice, BigDecimal roomPrice, HotelBooking hotelBooking) {
        BigDecimal totalPrice = BookingUtil.calculateTotalPriceOfBooking(servicePrice, roomPrice, hotelBooking.getDepositPrice());
        hotelBooking.setTotalPrice(totalPrice);
    }

    private static Transactions makeTransaction(BigDecimal totalPrice, HotelBooking hotelBooking, BigDecimal prePay, Long paymentTypeId) {
        Transactions transactions = new Transactions();
        transactions.setVnpayTransactionId(RandomKey.generateRandomKey());
        transactions.setStatus("Thành công");
        transactions.setAmount(totalPrice.subtract(prePay));
        transactions.setCreatedDate(Instant.now());
        transactions.setHotelBookingId(hotelBooking.getHotelBookingId());
        transactions.setPaymentId(paymentTypeId);
        return transactions;
    }

    private static BigDecimal calculateRoomPrice(List<BookingRoomDetails> hotelBookingDetails, BigDecimal roomPrice, Instant checkin,Instant checkout) {
        for (BookingRoomDetails bookingRoomDetail : hotelBookingDetails) {
            BigDecimal price = BookingUtil.calculatePriceBetweenDate(checkin, checkout, bookingRoomDetail.getRoomCategoryId(),true);
            roomPrice = roomPrice.add(price);
        }
        return roomPrice;
    }

    private static BigDecimal calculateServicePrice(List<SaveCheckoutHotelServiceDTO> hotelBookingServices, Map<Long, RoomService> allRoomServiceAsMap, BigDecimal servicePrice, HotelBooking hotelBooking, List<HotelBookingService> hotelBookingServiceList) {
        for (SaveCheckoutHotelServiceDTO hotelBookingService : hotelBookingServices) {
            RoomService roomService = allRoomServiceAsMap.get(hotelBookingService.getServiceId());
            servicePrice = servicePrice.add(roomService.getServicePrice().multiply(BigDecimal.valueOf(hotelBookingService.getQuantity())));

            HotelBookingService newHotelBookingService = new HotelBookingService();
            newHotelBookingService.setHotelBookingId(hotelBooking.getHotelBookingId());
            newHotelBookingService.setServiceId(roomService.getServiceId());
            newHotelBookingService.setCreateDate(Instant.now());
            newHotelBookingService.setRoomId(1L); // Mặc định là 1L trước về sau sẽ xóa
            hotelBookingServiceList.add(newHotelBookingService);
        }
        return servicePrice;
    }

    private void changeRoomStatus(List<BookingRoomDetails> hotelBookingDetails) {
        List<Long> bookedRoomIds = hotelBookingDetails.stream().map(BookingRoomDetails::getRoomId).collect(Collectors.toList());
        List<Room> bookedRooms = roomRepository.findAllById(bookedRoomIds);
        for (Room bookedRoom : bookedRooms) {
            bookedRoom.setRoomStatusId(3L);
        }
        roomRepository.saveAll(bookedRooms);
    }

    @Override
    public BigDecimal getTotalPriceOfHotelBooking(Long hotelBookingId) {
        HotelBooking hotelBooking = bookingRepository.findByHotelBookingId(hotelBookingId);
        if (hotelBooking != null) {
            return hotelBooking.getTotalPrice();
        }
        return null;
    }

    @Override
    public Boolean checkIn(Long hotelBookingId) {
        HotelBooking hotelBooking = bookingRepository.findByHotelBookingId(hotelBookingId);
        if (!BookingValidator.isValidToCheckIn(hotelBooking.getCheckIn())) {
            return false;
        }
        List<BookingRoomDetails> hotelBookingDetails = bookingRoomDetailsRepository.getAllByHotelBookingId(hotelBookingId);
        List<Long> allBookedRoomIds = hotelBookingDetails.stream().map(BookingRoomDetails::getRoomId).collect(Collectors.toList());
        List<Room> allBookedRooms = roomRepository.findAllById(allBookedRoomIds);
        boolean isExistNotReadyRoom = BookingValidator.isExistNotReadyRoom(allBookedRooms);
        if (isExistNotReadyRoom) {
            return false;
        }
        // Change status room / booking
        hotelBooking.setStatusId(2L);
        hotelBooking.setCheckIn(Instant.now());

        for (Room room : allBookedRooms) {
            room.setRoomStatusId(1L);
        }
        bookingRepository.save(hotelBooking);
        roomRepository.saveAll(allBookedRooms);
        return true;
    }

    @Override
    public List<HotelBooking> findAllWithStatusOneAndValidBooking(Boolean validBooking) {
        return bookingRepository.findByStatusIdAndValidBooking(1L, true);
    }

    @Override
    public Boolean checkIn(SaveCheckinDTO checkinDTO) {
        Long hotelBookingId = checkinDTO.getHotelBookingId();
        HotelBooking hotelBooking = bookingRepository.findByHotelBookingId(hotelBookingId);
        if (!BookingValidator.isValidToCheckIn(hotelBooking.getCheckIn())) {
            return false;
        }
        if (!hotelBooking.getStatusId().equals(1L)){
            return false;
        }
        List<BookingRoomDetails> hotelBookingDetails = bookingRoomDetailsRepository.getAllByHotelBookingId(hotelBookingId);
        Map<Long, SaveCheckinDetailDTO> newCheckinDetailMap = checkinDTO.getSaveCheckinDetailDTOS().stream()
                .collect(Collectors.toMap(SaveCheckinDetailDTO::getBookingRoomId, Function.identity()));

        // Update new room user choose when checkIn
        List<BookingRoomDetails> updateBookingRooms = hotelBookingDetails.stream()
                .map(hotelBookingDetail -> {
                    SaveCheckinDetailDTO chosenRoom = newCheckinDetailMap.get(hotelBookingDetail.getBookingRoomId());
                    Long bookingRoomId = hotelBookingDetail.getRoomId();
                    Long newBookingRoomId = chosenRoom.getRoomID();
                    if (!bookingRoomId.equals(newBookingRoomId)) {
                        hotelBookingDetail.setRoomId(chosenRoom.getRoomID());
                    }
                    return hotelBookingDetail;
                }).collect(Collectors.toList());


        List<Long> allBookedRoomIds = updateBookingRooms.stream().map(BookingRoomDetails::getRoomId).collect(Collectors.toList());

        List<Room> allBookedRooms = roomRepository.findAllById(allBookedRoomIds);
        boolean isExistNotReadyRoom = BookingValidator.isExistNotReadyRoom(allBookedRooms);
        if (isExistNotReadyRoom) {
            return false;
        }
        // Change status room / booking
        hotelBooking.setStatusId(2L);
        hotelBooking.setCheckIn(Instant.now());

        for (Room room : allBookedRooms) {
            room.setRoomStatusId(1L);
        }
        bookingRepository.save(hotelBooking);
        bookingRoomDetailsRepository.saveAll(updateBookingRooms);
        roomRepository.saveAll(allBookedRooms);
        return true;
    }
}
