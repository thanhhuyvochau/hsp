package fu.hbs.service.impl;

import fu.hbs.dto.CategoryRoomPriceDTO.DateInfoCategoryRoomPriceDTO;
import fu.hbs.dto.HotelBookingDTO.BookingDetailsDTO;
import fu.hbs.dto.HotelBookingDTO.RoomInDetailsDTO;
import fu.hbs.entities.*;
import fu.hbs.repository.*;
import fu.hbs.service.dao.BookingRoomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingRoomDetailsImpl implements BookingRoomDetailsService {
    @Value("${app.holidays.tetDuongLich}")
    private String tetDuongLichConfig;

    @Value("${app.holidays.ngayThongNhatDatNuoc}")
    private String ngayThongNhatDatNuocConfig;

    @Value("${app.holidays.ngayQuocTeLaoDong}")
    private String ngayQuocTeLaoDongConfig;

    @Value("${app.holidays.ngayQuocKhanh}")
    private String ngayQuocKhanhConfig;

    @Autowired
    BookingRoomDetailsRepository bookingRoomDetailsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HotelBookingRepository hotelBookingRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomCategoriesRepository roomCategoriesRepository;
    @Autowired
    CategoryRoomPriceRepository categoryRoomPriceRepository;


    @Override
    public BookingRoomDetails save(BookingRoomDetails bookingRoomDetails) {

        return bookingRoomDetailsRepository.save(bookingRoomDetails);
    }

    @Override
    public BookingDetailsDTO getBookingDetails(Authentication authentication, Long hotelBookingId) {
        BookingDetailsDTO dto = new BookingDetailsDTO();

        // User's details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.getUserByEmail(userDetails.getUsername());
        dto.setUser(user);

        // Hotel booking
        HotelBooking hotelBooking = hotelBookingRepository.findByHotelBookingId(hotelBookingId);

        LocalDate checkIn = hotelBooking.getCheckIn().toLocalDate();
        LocalDate checkOut = hotelBooking.getCheckOut().toLocalDate();

        dto.setHotelBooking(hotelBooking);
        // Booking details
        List<BookingRoomDetails> bookingRoomDetails = bookingRoomDetailsRepository.getAllByHotelBookingId(hotelBookingId);
        List<RoomInDetailsDTO> roomInDetailsDTOS = new ArrayList<>();
        ;

        Room room = new Room();
        RoomCategories roomCategories = new RoomCategories();
        CategoryRoomPrice categoryRoomPrice = new CategoryRoomPrice();
        List<Room> rooms = new ArrayList<>();
        int count = 0;
        Set<RoomCategories> distinctRoomCategories = new HashSet<>();

        for (BookingRoomDetails item : bookingRoomDetails) {
            room = roomRepository.findById(item.getRoomId()).get();
            rooms.add(room);
            roomCategories = roomCategoriesRepository.findByRoomCategoryId(item.getRoomCategoryId());
            distinctRoomCategories.add(roomCategories);
        }

        for (RoomCategories roomCategory : distinctRoomCategories) {
            roomInDetailsDTOS.add(new RoomInDetailsDTO(room, roomCategory, categoryRoomPriceRepository.findByRoomCategoryId(roomCategory.getRoomCategoryId())));
        }

        Map<Long, List<Room>> groupedRooms = rooms.stream().collect(Collectors.groupingBy(Room::getRoomCategoryId));
        for (Map.Entry<Long, List<Room>> entry : groupedRooms.entrySet()) {
            Long categoryId = entry.getKey();
            List<Room> roomsWithSameCategory = entry.getValue();
        }

//        List<DateInfoCategoryRoomPriceDTO> dateInfoList = processDateInfo(checkIn, checkOut);
//        BigDecimal total_Price = BigDecimal.ZERO;
//        Map<Long, BigDecimal> totalPriceByCategoryId = new HashMap<>();
//
//        for (CategoryRoomPrice cpr : categoryRoomPrices) {
//            BigDecimal totalForCategory = calculateTotalForCategory(cpr, dateInfoList);
//            totalPriceByCategoryId.put(cpr.getRoomCategoryId(), totalForCategory);
//
//        }
//
//        // Lặp qua map 1 và kiểm tra xem khóa có tồn tại trong map 2 không
//        for (Map.Entry<Long, Integer> entry1 : roomCategoryMap.entrySet()) {
//            Long category = entry1.getKey();
//            Integer roomCount = entry1.getValue();
//
//            if (totalPriceByCategoryId.containsKey(category)) {
//                // Nếu khóa tồn tại trong map 2, lấy giá trị từ cả hai map
//                BigDecimal totalPrice1 = totalPriceByCategoryId.get(category);
//
//                BigDecimal totalPrice2 = totalPrice1.multiply(BigDecimal.valueOf(roomCount));
//
//                total_Price = total_Price.add(totalPrice2);
//
//
//            }
//        }


        dto.setGroupedRooms(groupedRooms);
        dto.setBookingRoomDetails(roomInDetailsDTOS);

        return dto;
    }

    // Hàm tính tổng giá cho một CategoryRoomPrice dựa trên dateInfoList
    public BigDecimal calculateTotalForCategory(CategoryRoomPrice
                                                        cpr, List<DateInfoCategoryRoomPriceDTO> dateInfoList) {
        BigDecimal totalForCategory = BigDecimal.ZERO;
//        int daysBetween = calculateDaysBetween(dateInfoList.getDate(), endDate.getDate());
        int daysBetween = dateInfoList.size(); //

        for (int i = 0; i < daysBetween; i++) {
            BigDecimal multiplier = BigDecimal.ONE; // Mặc định là 1

            switch (dateInfoList.get(i).getDayType()) {
                case 2:
                    multiplier = new BigDecimal("1.5");
                    break;
                case 3:
                    multiplier = new BigDecimal("3");
                    break;
                default:
                    // Mặc định không thay đổi giá
                    break;
            }
            BigDecimal price = cpr.getPrice().multiply(multiplier); // Tính giá tiền cho cpr cụ thể
            totalForCategory = totalForCategory.add(price);
        }

        return totalForCategory;
    }


    private List<DateInfoCategoryRoomPriceDTO> processDateInfo(LocalDate startDate, LocalDate endDate) {
        List<DateInfoCategoryRoomPriceDTO> dateInfoList = new ArrayList<>();


        LocalDate startDate1 = startDate;
        LocalDate endDate1 = endDate;

        while (!startDate1.isAfter(endDate1)) {
            int dayType = getDayType(startDate1);

            dateInfoList.add(new DateInfoCategoryRoomPriceDTO(startDate1, dayType));
            startDate1 = startDate1.plusDays(1);
        }


        return dateInfoList;
    }

    /**
     * Get a list of holidays for a specific year.
     *
     * @param year The year for which holidays are requested.
     * @return A list of LocalDate objects representing holidays.
     */
    public List<LocalDate> getHolidays(int year) {
        List<LocalDate> holidays = new ArrayList<>();

        // Chuyển đổi các ngày lễ từ cấu hình thành LocalDate
        LocalDate tetDuongLich = LocalDate.parse(year + "-" + tetDuongLichConfig);
        LocalDate ngayThongNhatDatNuoc = LocalDate.parse(year + "-" + ngayThongNhatDatNuocConfig);
        LocalDate ngayQuocTeLaoDong = LocalDate.parse(year + "-" + ngayQuocTeLaoDongConfig);
        LocalDate ngayQuocKhanh = LocalDate.parse(year + "-" + ngayQuocKhanhConfig);

        holidays.add(tetDuongLich);
        holidays.add(ngayThongNhatDatNuoc);
        holidays.add(ngayQuocTeLaoDong);
        holidays.add(ngayQuocKhanh);

        return holidays;
    }

    /**
     * Determine the day type (weekday, weekend, or holiday) for a given date.
     *
     * @param startDate The date to determine the day type.
     * @return An integer representing the day type (1: weekday, 2: weekend, 3:
     * holiday).
     */
    private int getDayType(LocalDate startDate) {
        if (getHolidays(startDate.getYear()).contains(startDate)) {
            return 3; // holidays
        }
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();

        if (!(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
            return 1; // weekday
        } else {
            return 2; // weekend
        }
    }
}

