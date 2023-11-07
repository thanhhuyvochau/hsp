package fu.hbs.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fu.hbs.dto.HotelBookingDTO.CreateBookingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.dto.CategoryRoomPriceDTO.DateInfoCategoryRoomPriceDTO;
import fu.hbs.dto.HotelBookingDTO.ViewHotelBookingDTO;
import fu.hbs.entities.CategoryRoomFurniture;
import fu.hbs.entities.CategoryRoomPrice;
import fu.hbs.entities.HotelBooking;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomCategories;
import fu.hbs.entities.RoomFurniture;
import fu.hbs.entities.User;
import fu.hbs.repository.CategoryRoomFurnitureRepository;
import fu.hbs.repository.CategoryRoomPriceRepository;
import fu.hbs.repository.HotelBookingRepository;
import fu.hbs.repository.RoomCategoriesRepository;
import fu.hbs.repository.RoomFurnitureRepository;
import fu.hbs.repository.RoomRepository;
import fu.hbs.repository.UserRepository;
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

        for (int i = 0; i < hotelBookings.size(); i++) {
            ViewHotelBookingDTO viewHotelBookingDTO = new ViewHotelBookingDTO();
            roomCategories1 = roomCategoriesRepository.findByRoomCategoryId(hotelBookings.get(i).getRoomCategoryId());
            user = userRepository.findById(hotelBookings.get(i).getUserId()).get();
            viewHotelBookingDTO.setCheckOut(hotelBookings.get(i).getCheckOut());
            viewHotelBookingDTO.setCheckIn(hotelBookings.get(i).getCheckIn());
            viewHotelBookingDTO.setStatus(hotelBookings.get(i).getStatus());
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
    public HotelBookingAvailable findBookingsByDates(Date checkIn, Date checkOut, int numberPerson) {
        HotelBookingAvailable hotelBookingAvailable = new HotelBookingAvailable();

        List<Room> rooms = roomRepository.getAllRoom(checkIn, checkOut, numberPerson);

        List<RoomCategories> addedCategories = new ArrayList<>();
        RoomCategories categories = new RoomCategories();

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
        RoomCategories roomCategories = new RoomCategories();
        User user = new User();

        for (int i = 0; i < hotelBookings.size(); i++) {
            ViewHotelBookingDTO viewHotelBookingDTO = new ViewHotelBookingDTO();
            roomCategories = roomCategoriesRepository.findByRoomCategoryId(hotelBookings.get(i).getRoomCategoryId());
            roomCategoriesList.add(roomCategories);
            user = userRepository.findById(hotelBookings.get(i).getUserId()).get();
            viewHotelBookingDTO.setCheckOut(hotelBookings.get(i).getCheckOut());
            viewHotelBookingDTO.setCheckIn(hotelBookings.get(i).getCheckIn());
            viewHotelBookingDTO.setStatus(hotelBookings.get(i).getStatus());
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
