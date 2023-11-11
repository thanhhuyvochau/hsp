/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 08/11/2023    1.0        HieuLBM          First Deploy
 *
 */

package fu.hbs.service.impl;


import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import fu.hbs.dto.CancellationFormDTO;
import fu.hbs.dto.HotelBookingDTO.CreateBookingDTO;
import fu.hbs.entities.*;
import fu.hbs.exceptionHandler.RoomCategoryNamesNullException;
import fu.hbs.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.dto.CategoryRoomPriceDTO.DateInfoCategoryRoomPriceDTO;
import fu.hbs.dto.HotelBookingDTO.ViewHotelBookingDTO;
import fu.hbs.service.dao.HotelBookingService;
import fu.hbs.utils.StringDealer;

@Service
public class HotelBookingServiceImpl implements HotelBookingService {

    @Value("${app.holidays.tetDuongLich}")
    private String tetDuongLichConfig;

    @Value("${app.holidays.ngayThongNhatDatNuoc}")
    private String ngayThongNhatDatNuocConfig;

    @Value("${app.holidays.ngayQuocTeLaoDong}")
    private String ngayQuocTeLaoDongConfig;

    @Value("${app.holidays.ngayQuocKhanh}")
    private String ngayQuocKhanhConfig;

    @Autowired
    private HotelBookingRepository hotelBookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    RoomCategoriesRepository roomCategoriesRepository;
    @Autowired
    CategoryRoomFurnitureRepository categoryRoomFurnitureRepository;
    @Autowired
    RoomFurnitureRepository roomFurnitureRepository;
    @Autowired
    CategoryRoomPriceRepository categoryRoomPriceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoomStatusRepository roomStatusRepository;

    @Autowired
    CustomerCancellationReasonRepository customerCancellationReasonRepository;
    @Autowired
    CustomerCancellationRepository customerCancellationRepository;
    @Autowired
    RefundAccountRepository refundAccountRepository;
    StringDealer stringDealer;

    public HotelBookingServiceImpl() {
        this.stringDealer = new StringDealer();
    }

    @Override
    public List<ViewHotelBookingDTO> findAllByUserId(Long id) {
        List<HotelBooking> hotelBookings = hotelBookingRepository.findByUserId(id);
        List<ViewHotelBookingDTO> viewHotelBookingDTOList = new ArrayList<>();
        RoomCategories roomCategories1 = new RoomCategories();
        User user = new User();
        RoomStatus roomStatus = new RoomStatus();
        for (int i = 0; i < hotelBookings.size(); i++) {
            ViewHotelBookingDTO viewHotelBookingDTO = new ViewHotelBookingDTO();
            roomStatus = roomStatusRepository.findByStatusId(hotelBookings.get(i).getStatusId());
            user = userRepository.findById(hotelBookings.get(i).getUserId()).get();
            viewHotelBookingDTO.setHotelBookingId(hotelBookings.get(i).getHotelBookingId());
            viewHotelBookingDTO.setCheckOut(hotelBookings.get(i).getCheckOut());
            viewHotelBookingDTO.setCheckIn(hotelBookings.get(i).getCheckIn());
            viewHotelBookingDTO.setStatusId(roomStatus);
            viewHotelBookingDTO.setUser(user);
            viewHotelBookingDTOList.add(viewHotelBookingDTO);
        }
        System.out.println(viewHotelBookingDTOList);
        return viewHotelBookingDTOList;
    }

    /**
     * Find available hotel bookings based on check-in, check-out, and number of
     * persons.
     *
     * @param checkIn      The check-in date.
     * @param checkOut     The check-out date.
     * @param numberPerson The number of persons.
     * @return An instance of HotelBookingAvailable with available rooms and related
     * information.
     */
    public HotelBookingAvailable findBookingsByDates(Date checkIn, Date checkOut, int numberPerson) throws RoomCategoryNamesNullException {
        HotelBookingAvailable hotelBookingAvailable = new HotelBookingAvailable();

        List<Room> rooms = roomRepository.getAllRoom(checkIn, checkOut, numberPerson);

        List<RoomCategories> addedCategories = new ArrayList<>();
        RoomCategories categories = new RoomCategories();

        if (addedCategories == null) {
            throw new RoomCategoryNamesNullException("Khônng có phòng nào trong khoảng thời gian này");
        }

        // Group rooms by room category
        Map<Long, List<Room>> groupedRooms = rooms.stream().collect(Collectors.groupingBy(Room::getRoomCategoryId));
        for (Map.Entry<Long, List<Room>> entry : groupedRooms.entrySet()) {
            Long categoryId = entry.getKey();
            List<Room> roomsWithSameCategory = entry.getValue();
            addedCategories.add(roomCategoriesRepository.findDistinctByRoomCategoryId(categoryId));
        }


        List<CategoryRoomFurniture> categoryRoomFurnitures = new ArrayList<>();
        // CategoryRoomFurniture categoryRoomFurniture = new CategoryRoomFurniture();
        for (int i = 0; i < addedCategories.size(); i++) {
            categoryRoomFurnitures = categoryRoomFurnitureRepository
                    .findByRoomCategoryId(addedCategories.get(i).getRoomCategoryId());
        }

        List<RoomFurniture> roomFurnitures = new ArrayList<>();
        RoomFurniture roomFurniture = new RoomFurniture();
        for (int i = 0; i < categoryRoomFurnitures.size(); i++) {
            roomFurniture = roomFurnitureRepository.findByFurnitureId(categoryRoomFurnitures.get(i).getFurnitureId());
            roomFurnitures.add(roomFurniture);
        }

        CategoryRoomPrice categoryRoomPrice = new CategoryRoomPrice();
        List<CategoryRoomPrice> categoryRoomPrices = new ArrayList<>();
        for (int i = 0; i < addedCategories.size(); i++) {
            categoryRoomPrice = categoryRoomPriceRepository.getCategoryId(addedCategories.get(i).getRoomCategoryId());

            categoryRoomPrices.add(categoryRoomPrice);
        }


        LocalDate startDate = stringDealer.convertDateToLocalDate(checkIn);
        LocalDate endDate = stringDealer.convertDateToLocalDate(checkOut);

        List<DateInfoCategoryRoomPriceDTO> dateInfoList = new ArrayList<>();
        // xử lí day_type
        for (int i = 0; i < categoryRoomPrices.size(); i++) {
            LocalDate startDate1 = startDate;
            LocalDate endDate1 = endDate;

            while (!startDate1.isAfter(endDate1)) {
                int dayType = getDayType(startDate1);

                dateInfoList.add(new DateInfoCategoryRoomPriceDTO(startDate1, dayType));
                startDate1 = startDate1.plusDays(1);
            }
            break;
        }

        hotelBookingAvailable.setRooms(rooms);
        hotelBookingAvailable.setRoomCategories(addedCategories);
        hotelBookingAvailable.setCategoryRoomFurnitures(categoryRoomFurnitures);
        hotelBookingAvailable.setRoomFurnitures(roomFurnitures);
        hotelBookingAvailable.setTotalRoom(groupedRooms);
        hotelBookingAvailable.setCategoryRoomPrices(categoryRoomPrices);
        hotelBookingAvailable.setDateInfoCategoryRoomPriceDTOS(dateInfoList);

        return hotelBookingAvailable;
    }

    @Override
    public CreateBookingDTO createBooking(Long categoryId, LocalDate checkIn, LocalDate checkOut) {
        CreateBookingDTO createBookingDTO = new CreateBookingDTO();
        RoomCategories roomCategories = roomCategoriesRepository.findByRoomCategoryId(categoryId);
        // Lấy ra rooms còn trống
        List<Room> rooms = roomRepository.findAvailableRoomsByCategoryId(categoryId, checkIn, checkOut);

        return createBookingDTO;
    }

    @Override
    public List<ViewHotelBookingDTO> findAllByUserIdAndSameTime(Long id) {
        List<HotelBooking> hotelBookings = hotelBookingRepository.findAllByUserId(id);
        List<ViewHotelBookingDTO> viewHotelBookingDTOList = new ArrayList<>();
        List<RoomCategories> roomCategoriesList = new ArrayList<>();
        User user = new User();
        RoomStatus roomStatus = new RoomStatus();
        for (int i = 0; i < hotelBookings.size(); i++) {
            ViewHotelBookingDTO viewHotelBookingDTO = new ViewHotelBookingDTO();
            roomStatus = roomStatusRepository.findByStatusId(hotelBookings.get(i).getStatusId());
            user = userRepository.findById(hotelBookings.get(i).getUserId()).get();
            viewHotelBookingDTO.setHotelBookingId(hotelBookings.get(i).getHotelBookingId());
            viewHotelBookingDTO.setCheckOut(hotelBookings.get(i).getCheckOut());
            viewHotelBookingDTO.setCheckIn(hotelBookings.get(i).getCheckIn());
            viewHotelBookingDTO.setStatusId(roomStatus);
            viewHotelBookingDTO.setTotalRoom(hotelBookings.get(i).getTotalRoom());
            viewHotelBookingDTO.setUser(user);
            viewHotelBookingDTO.setRoomCategoriesList(roomCategoriesList);
            viewHotelBookingDTOList.add(viewHotelBookingDTO);
        }
        System.out.println(viewHotelBookingDTOList);
        return viewHotelBookingDTOList;
    }

    @Override
    public HotelBooking save(HotelBooking hotelBooking) {
        return hotelBookingRepository.save(hotelBooking);
    }

    @Override
    public CreateBookingDTO createBooking(List<Long> roomCategoryNames, List<Integer> selectedRoomCategories, LocalDate checkIn, LocalDate checkOut, HttpSession session) {
        CreateBookingDTO createBookingDTO = new CreateBookingDTO();
        Map<Long, Integer> roomCategoryMap = new HashMap<>();
        Integer number = (Integer) session.getAttribute("numberOfPeople");

        // Lấy ra các Loại phòng và số phòng còn trống
        if (roomCategoryNames.size() == selectedRoomCategories.size()) {
            for (int i = 0; i < roomCategoryNames.size(); i++) {
                Long category = roomCategoryNames.get(i);
                int roomCount = selectedRoomCategories.get(i);
                if (roomCount > 0) {
                    roomCategoryMap.put(category, roomCount);
                }
            }
        }
        if (roomCategoryMap.isEmpty()) {
            throw new RuntimeException("Bạn chưa đặt phòng nào");
        }

        List<Room> rooms = new ArrayList<>();
        List<RoomCategories> roomCategoriesList = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : roomCategoryMap.entrySet()) {
            Long category = entry.getKey();
            rooms = roomRepository.findAvailableRoomsByCategoryId(category, checkIn, checkOut);
            roomCategoriesList.add(roomCategoriesRepository.findByRoomCategoryId(category));
        }


        for (Map.Entry<Long, Integer> entry : roomCategoryMap.entrySet()) {
            Long category = entry.getKey();
            List<Room> roomsByCategory = roomRepository.findAvailableRoomsByCategoryId(category, checkIn, checkOut);
            rooms.addAll(roomsByCategory);
        }

        // Group rooms by room category
        Map<Long, List<Room>> roomMap = rooms.stream().collect(Collectors.groupingBy(Room::getRoomCategoryId));
        for (Map.Entry<Long, List<Room>> entry : roomMap.entrySet()) {
            Long categoryId = entry.getKey();
            List<Room> roomsWithSameCategory = entry.getValue();

        }


        List<CategoryRoomPrice> categoryRoomPrices = new ArrayList<>();

        // Price theo Category
        for (RoomCategories roomCategory : roomCategoriesList) {
            CategoryRoomPrice categoryRoomPrice = categoryRoomPriceRepository.getCategoryId(roomCategory.getRoomCategoryId());
            categoryRoomPrices.add(categoryRoomPrice);
        }

        List<DateInfoCategoryRoomPriceDTO> dateInfoList = processDateInfo(checkIn, checkOut);
        BigDecimal total_Price = BigDecimal.ZERO;
        Map<Long, BigDecimal> totalPriceByCategoryId = new HashMap<>();

        for (CategoryRoomPrice cpr : categoryRoomPrices) {
            BigDecimal totalForCategory = calculateTotalForCategory(cpr, dateInfoList);
            totalPriceByCategoryId.put(cpr.getRoomCategoryId(), totalForCategory);

        }

        // Lặp qua map 1 và kiểm tra xem khóa có tồn tại trong map 2 không
        for (Map.Entry<Long, Integer> entry1 : roomCategoryMap.entrySet()) {
            Long category = entry1.getKey();
            Integer roomCount = entry1.getValue();

            if (totalPriceByCategoryId.containsKey(category)) {
                // Nếu khóa tồn tại trong map 2, lấy giá trị từ cả hai map
                BigDecimal totalPrice1 = totalPriceByCategoryId.get(category);

                BigDecimal totalPrice2 = totalPrice1.multiply(BigDecimal.valueOf(roomCount));

                total_Price = total_Price.add(totalPrice2);


            }
        }


        createBookingDTO.setRoomCategoriesList(roomCategoriesList);
        createBookingDTO.setTotalPrice(total_Price);
        createBookingDTO.setCheckIn(checkIn);
        createBookingDTO.setCheckOut(checkOut);
        createBookingDTO.setRoomCategoryMap(roomCategoryMap);
        createBookingDTO.setTotalPriceByCategoryId(totalPriceByCategoryId);
        createBookingDTO.setDateInfoList(dateInfoList);

        return createBookingDTO;


    }

    @Override
    public void cancelBooking(CancellationFormDTO cancellationFormDTO, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.getUserByEmail(userDetails.getUsername());

        // RefundAccount
        RefundAccount refundAccount = new RefundAccount();
        refundAccount.setAccountNumber(cancellationFormDTO.getAccountNumber());
        refundAccount.setAccountName(cancellationFormDTO.getAccountName());
        refundAccount.setBankId(cancellationFormDTO.getBankId());
        refundAccount.setUserId(user.getUserId());

        RefundAccount newRefundAccount = refundAccountRepository.save(refundAccount);


        // Cancellation
        CustomerCancellation customerCancellation = new CustomerCancellation();
        customerCancellation.setHotelBookingId(cancellationFormDTO.getHotelBookingId());
        customerCancellation.setCancelTime(new Date());
        customerCancellation.setReasonId(cancellationFormDTO.getReasonId());
        customerCancellation.setOtherReason(cancellationFormDTO.getOtherReason());
        customerCancellation.setAccountId(newRefundAccount.getAccountId());
        customerCancellation.setStatus(false);
        customerCancellationRepository.save(customerCancellation);
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
