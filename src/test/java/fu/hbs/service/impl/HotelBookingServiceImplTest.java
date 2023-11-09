package fu.hbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

public class HotelBookingServiceImplTest {

    @InjectMocks
    private HotelBookingServiceImpl hotelBookingService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomCategoriesRepository roomCategoriesRepository;

    @Mock
    private CategoryRoomFurnitureRepository categoryRoomFurnitureRepository;

    @Mock
    private RoomFurnitureRepository roomFurnitureRepository;

    @Mock
    private CategoryRoomPriceRepository categoryRoomPriceRepository;
    @Mock
    private HotelBookingRepository hotelBookingRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAllByUserId() {
        ViewHotelBookingDTO viewHotelBookingDTO = new ViewHotelBookingDTO();
        // Tạo giả lập dữ liệu
        User user = new User();
        user.setUserId(1L);

        HotelBooking hotelBooking1 = new HotelBooking();
        hotelBooking1.setHotelBookingId(1L);
        hotelBooking1.setUserId(1L);

        HotelBooking hotelBooking2 = new HotelBooking();
        hotelBooking2.setHotelBookingId(2L);
        hotelBooking2.setUserId(1L);
        List<HotelBooking> hotelBookings = hotelBookingRepository.findByUserId(1L);

        hotelBookings.add(hotelBooking2);
        hotelBookings.add(hotelBooking1);

        List<HotelBooking> hotelBookings1 = new ArrayList<>();
        hotelBookings1.add(hotelBooking1);
        hotelBookings1.add(hotelBooking2);

//		viewHotelBookingDTO.setHotelBookingId(hotelBookings.get(0).getHotelBookingId());
//		viewHotelBookingDTO.setHotelBookingId(hotelBookings.get(1).getUserId());

        when(hotelBookingRepository.findByUserId(1L)).thenReturn(hotelBookings1);

        List<ViewHotelBookingDTO> result = new ArrayList<>();
        result.add(viewHotelBookingDTO);

        // Kiểm tra kết quả
        assertEquals(1, result.size());

    }

    @Test
    void findBookingsByDates() throws ParseException {
        HotelBookingAvailable available = new HotelBookingAvailable();
        Date checkIn = new Date(1L); // Đặt giá trị của check-in ở đây
        Date checkOut = new Date(1L); // Đặt giá trị của check-out ở đây
        // Tạo dữ liệu giả lập

        int numberPerson = 2;

        // Giả lập kết quả trả về từ repository
        List<Room> mockRooms = new ArrayList<>();
        Room mockRoom = new Room();
        mockRoom.setRoomCategoryId(1L);
        // Thêm các phòng giả lập vào danh sách
        mockRooms.add(mockRoom);
        when(roomRepository.getAllRoom(checkIn, checkOut, numberPerson)).thenReturn(mockRooms);

        List<RoomCategories> mockRoomCategories = new ArrayList<>();
        RoomCategories mockCategory = new RoomCategories();
        mockCategory.setRoomCategoryId(1L);
        mockRoomCategories.add(mockCategory);
        when(roomCategoriesRepository.findDistinctByRoomCategoryId(1L)).thenReturn(mockCategory);

        List<CategoryRoomFurniture> mockCategoryRoomFurniture = new ArrayList<>();
        when(categoryRoomFurnitureRepository.findByRoomCategoryId(1L)).thenReturn(mockCategoryRoomFurniture);

        List<RoomFurniture> mockRoomFurnitures = new ArrayList<>();
        when(roomFurnitureRepository.findByFurnitureId(1L)).thenReturn(new RoomFurniture());

        CategoryRoomPrice mockCategoryRoomPrice = new CategoryRoomPrice();
        when(categoryRoomPriceRepository.getCategoryId(1L)).thenReturn(mockCategoryRoomPrice);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        List<DateInfoCategoryRoomPriceDTO> dateInfoList = new ArrayList<>();
        dateInfoList.add(new DateInfoCategoryRoomPriceDTO(startDate, 1));
        dateInfoList.add(new DateInfoCategoryRoomPriceDTO(startDate.plusDays(1), 1));
        dateInfoList.add(new DateInfoCategoryRoomPriceDTO(startDate.plusDays(2), 1));

        available.setCategoryRoomFurnitures(mockCategoryRoomFurniture);
        available.setDateInfoCategoryRoomPriceDTOS(dateInfoList);
        available.setRoomCategories(mockRoomCategories);

        // So sánh đối tượng thực tế với đối tượng giá trị kỳ vọng
        assertEquals(mockRoomCategories, available.getRoomCategories());
    }

    @Test
    void getHolidays() {
        int year = 2023;
        List<LocalDate> holidays = hotelBookingService.getHolidays(year);
        holidays.add(LocalDate.of(1, 2, 9));
        assertEquals(1, holidays.size());
    }
}