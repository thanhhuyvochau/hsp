package fu.hbs.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import fu.hbs.dto.HotelBookingDTO.*;
import fu.hbs.dto.RoomServiceDTO.RoomBookingServiceDTO;
import fu.hbs.entities.*;
import fu.hbs.exceptionHandler.NotEnoughRoomAvalaibleException;
import fu.hbs.repository.*;
import fu.hbs.utils.BookingUtil;
import fu.hbs.utils.RandomKey;
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
    private VnpayTransactionsRepository vnpayTransactionsRepository;

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
    public void createHotelBookingByReceptionist(CreateHotelBookingDTO bookingRequest) {
        // Create basic object booking and save to get the id
        HotelBooking hotelBooking = new HotelBooking();
        hotelBooking.setUserId(null);
        hotelBooking.setStatusId(1L);
        hotelBooking.setName(bookingRequest.getName());
        hotelBooking.setEmail(bookingRequest.getEmail());
        hotelBooking.setAddress(bookingRequest.getAddress());
        hotelBooking.setPhone(bookingRequest.getPhone());

        Instant checkIn = bookingRequest.getCheckIn();
        Instant checkOut = bookingRequest.getCheckOut();

        hotelBooking.setCheckIn(checkIn);
        hotelBooking.setCheckOut(checkOut);

        // Calculate total room number
        Optional<Integer> totalRoomNumberOp = bookingRequest.getBookingDetails().stream().map(CreateHotelBookingDetailDTO::getRoomNumber).reduce(Integer::sum);
        totalRoomNumberOp.ifPresent(hotelBooking::setTotalRoom);

        // Save to get id before create Booking Details
        this.save(hotelBooking);

        // Base on request and then create booking detail
        List<BookingRoomDetails> bookingDetails = createBookingDetails(bookingRequest, checkIn, checkOut, hotelBooking);
        bookingRoomDetailsRepository.saveAll(bookingDetails);
        BigDecimal totalPrice = this.calculateTotalPrice(bookingDetails);
        hotelBooking.setTotalPrice(totalPrice);
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
                Room room = availableRoomsByCategoryIdAndDateBetween.get(i);
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

    private BigDecimal calculateTotalPrice(List<BookingRoomDetails> bookingRoomDetailsList) {
        List<Long> bookRoomIds = bookingRoomDetailsList.stream().map(BookingRoomDetails::getRoomId).collect(Collectors.toList());
        Map<Long, Room> bookRoomMap = roomRepository.findAllById(bookRoomIds).stream().collect(Collectors.toMap(Room::getRoomId, Function.identity()));
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BookingRoomDetails bookingRoomDetails : bookingRoomDetailsList) {
            Room room = bookRoomMap.get(bookingRoomDetails.getRoomId());
            totalPrice = totalPrice.add(room.getPrice());
        }
        return totalPrice;
    }

    @Override
    public boolean checkout(SaveCheckoutDTO saveCheckoutDTO) {
        Optional<HotelBooking> hotelBookingOptional = bookingRepository.findById(saveCheckoutDTO.getHotelBookingId());
        if (hotelBookingOptional.isPresent()) {
            // Create VnPay transaction
            HotelBooking hotelBooking = hotelBookingOptional.get();
            List<SaveCheckoutHotelServiceDTO> hotelBookingServices = saveCheckoutDTO.getHotelServices();
            hotelBookingServiceRepository.deleteAllByHotelBookingId(hotelBooking.getHotelBookingId());
            Map<Long, RoomService> allRoomServiceAsMap = BookingUtil.getAllRoomServiceAsMap();


            BigDecimal servicePrice = BigDecimal.ZERO;
            List<HotelBookingService> hotelBookingServiceList = new ArrayList<>();
            for (SaveCheckoutHotelServiceDTO hotelBookingService : hotelBookingServices) {
                RoomService roomService = allRoomServiceAsMap.get(hotelBookingService.getServiceId());
                servicePrice = servicePrice.add(roomService.getServicePrice().multiply(BigDecimal.valueOf(hotelBookingService.getQuantity())));

                HotelBookingService newHotelBookingService = new HotelBookingService();
                newHotelBookingService.setHotelBookingId(hotelBooking.getHotelBookingId());
                newHotelBookingService.setServiceId(roomService.getServiceId());
                newHotelBookingService.setQuantity(hotelBookingService.getQuantity());
                hotelBookingServiceList.add(newHotelBookingService);
            }


            BigDecimal roomPrice = BigDecimal.ZERO;
            List<BookingRoomDetails> allByHotelBookingId = bookingRoomDetailsRepository.getAllByHotelBookingId(hotelBooking.getHotelBookingId());
            Instant checkIn = hotelBooking.getCheckIn();
            for (BookingRoomDetails bookingRoomDetail : allByHotelBookingId) {
                BigDecimal price = BookingUtil.calculatePriceBetweenDate(checkIn, hotelBooking.getCheckOut(), bookingRoomDetail.getRoomCategoryId());
                roomPrice = roomPrice.add(price);
            }

            hotelBooking.setCheckIn(checkIn);


            BigDecimal taxPrice = servicePrice.add(roomPrice).multiply(BigDecimal.valueOf(0.1));
            BigDecimal totalPrice = servicePrice.add(roomPrice).add(taxPrice);

            VnpayTransactions transactions = new VnpayTransactions();
            transactions.setTransactionId(RandomKey.generateRandomKey());
            transactions.setStatus("Thành công");
            transactions.setAmount(totalPrice);
            LocalDate localDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
            Date date = Date.valueOf(localDate);
            transactions.setCreatedDate(date);
            transactions.setHotelBookingId(hotelBooking.getHotelBookingId());

            vnpayTransactionsRepository.save(transactions);
            hotelBookingServiceRepository.saveAll(hotelBookingServiceList);
            bookingRepository.save(hotelBooking);
            return true;
        }
        return false;
    }
}
