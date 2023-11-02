package fu.hbs.service.impl;

import fu.hbs.dto.CategoryRoomPriceDTO.DateInfoCategoryRoomPriceDTO;
import fu.hbs.dto.HotelBookingAvailable;
import fu.hbs.entities.*;
import fu.hbs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelBookingServiceImpl {
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


    public HotelBookingAvailable findBookingsByDates(Date checkIn, Date checkOut, int numberPerson) {
        HotelBookingAvailable hotelBookingAvailable = new HotelBookingAvailable();

        List<Room> rooms = roomRepository.getAllRoom(checkIn, checkOut, numberPerson);


        List<RoomCategories> addedCategories = new ArrayList<>();
        RoomCategories categories = new RoomCategories();
        Map<Long, List<Room>> groupedRooms = rooms.stream()
                .collect(Collectors.groupingBy(Room::getRoomCategoryId));
        for (Map.Entry<Long, List<Room>> entry : groupedRooms.entrySet()) {
            Long categoryId = entry.getKey();
            List<Room> roomsWithSameCategory = entry.getValue();
            addedCategories.add(roomCategoriesRepository.findDistinctByRoomCategoryId(categoryId));
        }


        List<CategoryRoomFurniture> categoryRoomFurnitures = new ArrayList<>();
//        CategoryRoomFurniture categoryRoomFurniture = new CategoryRoomFurniture();
        for (int i = 0; i < addedCategories.size(); i++) {
            categoryRoomFurnitures = categoryRoomFurnitureRepository.findByRoomCategoryId(addedCategories.get(i).getRoomCategoryId());
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


        LocalDate startDate = convertDateToLocalDate(checkIn);
        LocalDate endDate = convertDateToLocalDate(checkOut);
        List<DateInfoCategoryRoomPriceDTO> dateInfoList = new ArrayList<>();
        // xử lí day_type
        for (int i = 0; i < categoryRoomPrices.size(); i++) {
            LocalDate startDate1 = startDate;
            LocalDate endDate1 = endDate;

            while (!startDate1.isAfter(endDate1)) {
                DayOfWeek dayOfWeek = startDate1.getDayOfWeek();
                int dayType = getDayType(startDate1);

                dateInfoList.add(new DateInfoCategoryRoomPriceDTO(startDate1, dayType));
                startDate1 = startDate1.plusDays(1);
            }

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

    private boolean isHoliday(LocalDate startDate) {
        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2023, 2, 1)); // Tết Nguyên Đán (Lunar New Year)
        holidays.add(LocalDate.of(2023, 2, 2)); // Tết Nguyên Đán (Lunar New Year)
        holidays.add(LocalDate.of(2023, 9, 22)); // Tết Trung Thu (Mid-Autumn Festival)
        holidays.add(LocalDate.of(2023, 4, 30)); // Ngày Thống nhất Đất nước (Reunification Day)
        holidays.add(LocalDate.of(2023, 5, 1)); // Ngày Quốc tế Lao động (International Workers' Day)
        holidays.add(LocalDate.of(2023, 9, 2)); // Ngày Quốc khánh (National Day)


        return holidays.contains(startDate);
    }

    private int getDayType(LocalDate startDate) {


        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2023, 2, 1)); // Tết Nguyên Đán (Lunar New Year)
        holidays.add(LocalDate.of(2023, 2, 2)); // Tết Nguyên Đán (Lunar New Year)
        holidays.add(LocalDate.of(2023, 9, 22)); // Tết Trung Thu (Mid-Autumn Festival)
        holidays.add(LocalDate.of(2023, 4, 30)); // Ngày Thống nhất Đất nước (Reunification Day)
        holidays.add(LocalDate.of(2023, 5, 1)); // Ngày Quốc tế Lao động (International Workers' Day)
        holidays.add(LocalDate.of(2023, 9, 2)); // Ngày Quốc khánh (National Day)

        if (holidays.contains(startDate)) {
            return 3;
        }
        
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();

        if (!(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
            return 1;
        } else {
            return 2;
        }
    }

    public LocalDate convertDateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault(); // Sử dụng múi giờ hệ thống
        return instant.atZone(zoneId).toLocalDate();
    }

}


