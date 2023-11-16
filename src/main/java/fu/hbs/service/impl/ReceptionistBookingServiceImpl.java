package fu.hbs.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import fu.hbs.dto.HotelBookingDTO.ViewCheckoutDTO;
import fu.hbs.dto.HotelBookingDTO.CreateHotelBookingDTO;
import fu.hbs.dto.HotelBookingDTO.CreateHotelBookingDetailDTO;
import fu.hbs.dto.RoomServiceDTO.RoomBookingServiceDTO;
import fu.hbs.entities.*;
import fu.hbs.exceptionHandler.NotEnoughRoomAvalaibleException;
import fu.hbs.repository.BookingRoomDetailsRepository;
import fu.hbs.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import fu.hbs.repository.HotelBookingRepository;
import fu.hbs.repository.RoomStatusRepository;
import fu.hbs.service.dao.ReceptionistBookingService;
import org.springframework.transaction.annotation.Transactional;

public class ReceptionistBookingServiceImpl implements ReceptionistBookingService {
    @Autowired
    private HotelBookingRepository bookingRepository;
    @Autowired
    private RoomStatusRepository roomStatusRepository; // Autowire RoomStatusRepository
    @Autowired
    private BookingRoomDetailsRepository bookingRoomDetailsRepository;

    @Autowired
    private RoomRepository roomRepository;

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

        Date checkIn = bookingRequest.getCheckIn();
        Date checkOut = bookingRequest.getCheckOut();

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

    private List<BookingRoomDetails> createBookingDetails(CreateHotelBookingDTO bookingRequest, Date checkIn, Date checkOut, HotelBooking hotelBooking) {
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

    private List<Room> findAvailableRoomsByCategoryIdAndDateBetween(Date start, Date end, Long roomCategoryId) {
        return roomRepository.findAvailableRoomsByCategoryId(roomCategoryId, start.toLocalDate(), end.toLocalDate());
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
    public boolean checkout(ViewCheckoutDTO checkoutDTO) {
        Optional<HotelBooking> hotelBookingOptional = bookingRepository.findById(checkoutDTO.getHotelBookingId());
        if (hotelBookingOptional.isPresent()) {
            // Create VnPay transaction
            HotelBooking hotelBooking = hotelBookingOptional.get();
            VnpayTransactions transactions = new VnpayTransactions();
            transactions.setTransactionId("");
            transactions.setStatus("Thành công");
            transactions.setAmount(checkoutDTO.getTotalPrice());
            LocalDate localDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
            Date date = Date.valueOf(localDate);
            transactions.setCreatedDate(date);
            transactions.setHotelBookingId(hotelBooking.getHotelBookingId());

            List<RoomBookingServiceDTO> roomBookingServiceDTOS = checkoutDTO.getRoomBookingServiceDTOS();
//            for (RoomBookingServiceDTO roomBookingServiceDTO : roomBookingServiceDTOS) {
//                Long roomServiceId = roomBookingServiceDTO.getRoomServiceId();
//                int quantity = roomBookingServiceDTO.getQuantity();
//
//            }
        }

        return false;
    }
}
