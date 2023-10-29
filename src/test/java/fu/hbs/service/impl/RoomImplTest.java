package fu.hbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fu.hbs.dto.BookRoomByCategory;
import fu.hbs.entities.CategoryRoomFurniture;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomFurniture;
import fu.hbs.entities.RoomImage;
import fu.hbs.repository.CategoryRoomFurnitureRepository;
import fu.hbs.repository.RoomCategoriesRepository;
import fu.hbs.repository.RoomFurnitureRepository;
import fu.hbs.repository.RoomImageRepository;
import fu.hbs.repository.RoomRepository;
import fu.hbs.repository.RoomStatusRepository;
import fu.hbs.repository.ServiceRepository;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RoomImpl.class})
@ExtendWith(SpringExtension.class)
class RoomImplTest {
    @MockBean
    private CategoryRoomFurnitureRepository categoryRoomFurnitureRepository;

    @MockBean(name = "roomCategoriesRepository")
    private RoomCategoriesRepository roomCategoriesRepository;

    @MockBean
    private RoomFurnitureRepository roomFurnitureRepository;

    @MockBean
    private RoomImageRepository roomImageRepository;

    @Autowired
    private RoomImpl roomImpl;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private RoomStatusRepository roomStatusRepository;

    @MockBean
    private ServiceRepository serviceRepository;

    /**
     * Method under test: {@link RoomImpl#getRoom(Long)}
     */
    @Test
    void testGetRoom() {
        when(categoryRoomFurnitureRepository.findByRoomCategoryId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(roomRepository.findByRoomCategoryId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(serviceRepository.findAll()).thenReturn(new ArrayList<>());
        BookRoomByCategory actualRoom = roomImpl.getRoom(1L);
        List<CategoryRoomFurniture> categoryRoomFurnitures = actualRoom.getCategoryRoomFurnitures();
        assertTrue(categoryRoomFurnitures.isEmpty());
        assertEquals(categoryRoomFurnitures, actualRoom.getServices());
        assertEquals(categoryRoomFurnitures, actualRoom.getRooms());
        assertEquals(categoryRoomFurnitures, actualRoom.getRoomFurnitures());
        assertEquals(categoryRoomFurnitures, actualRoom.getImages());
        verify(categoryRoomFurnitureRepository).findByRoomCategoryId(Mockito.<Long>any());
        verify(roomRepository).findByRoomCategoryId(Mockito.<Long>any());
        verify(serviceRepository).findAll();
    }

    /**
     * Method under test: {@link RoomImpl#getRoom(Long)}
     */
    @Test
    void testGetRoom2() {
        CategoryRoomFurniture categoryRoomFurniture = new CategoryRoomFurniture();
        categoryRoomFurniture.setFurnitureId(1L);
        categoryRoomFurniture.setId(1L);
        categoryRoomFurniture.setNote("Note");
        categoryRoomFurniture.setQuantity(1);
        categoryRoomFurniture.setRoomCategoryId(1L);
        categoryRoomFurniture.setStatus(true);

        ArrayList<CategoryRoomFurniture> categoryRoomFurnitureList = new ArrayList<>();
        categoryRoomFurnitureList.add(categoryRoomFurniture);
        when(categoryRoomFurnitureRepository.findByRoomCategoryId(Mockito.<Long>any()))
                .thenReturn(categoryRoomFurnitureList);

        RoomFurniture roomFurniture = new RoomFurniture();
        roomFurniture.setFurnitureId(1L);
        roomFurniture.setFurnitureName("Furniture Name");
        roomFurniture.setFurniturePrice(new BigDecimal("2.3"));
        roomFurniture.setStatus(true);
        when(roomFurnitureRepository.findByFurnitureId(Mockito.<Long>any())).thenReturn(roomFurniture);
        ArrayList<Room> roomList = new ArrayList<>();
        when(roomRepository.findByRoomCategoryId(Mockito.<Long>any())).thenReturn(roomList);
        when(serviceRepository.findAll()).thenReturn(new ArrayList<>());
        BookRoomByCategory actualRoom = roomImpl.getRoom(1L);
        assertEquals(1, actualRoom.getCategoryRoomFurnitures().size());
        assertEquals(roomList, actualRoom.getServices());
        List<Room> rooms = actualRoom.getRooms();
        assertTrue(rooms.isEmpty());
        assertEquals(1, actualRoom.getRoomFurnitures().size());
        assertEquals(rooms, actualRoom.getImages());
        verify(categoryRoomFurnitureRepository).findByRoomCategoryId(Mockito.<Long>any());
        verify(roomFurnitureRepository).findByFurnitureId(Mockito.<Long>any());
        verify(roomRepository).findByRoomCategoryId(Mockito.<Long>any());
        verify(serviceRepository).findAll();
    }

    /**
     * Method under test: {@link RoomImpl#getRoom(Long)}
     */
    @Test
    void testGetRoom3() {
        CategoryRoomFurniture categoryRoomFurniture = new CategoryRoomFurniture();
        categoryRoomFurniture.setFurnitureId(1L);
        categoryRoomFurniture.setId(1L);
        categoryRoomFurniture.setNote("Note");
        categoryRoomFurniture.setQuantity(1);
        categoryRoomFurniture.setRoomCategoryId(1L);
        categoryRoomFurniture.setStatus(true);

        ArrayList<CategoryRoomFurniture> categoryRoomFurnitureList = new ArrayList<>();
        categoryRoomFurnitureList.add(categoryRoomFurniture);
        when(categoryRoomFurnitureRepository.findByRoomCategoryId(Mockito.<Long>any()))
                .thenReturn(categoryRoomFurnitureList);

        RoomFurniture roomFurniture = new RoomFurniture();
        roomFurniture.setFurnitureId(1L);
        roomFurniture.setFurnitureName("Furniture Name");
        roomFurniture.setFurniturePrice(new BigDecimal("2.3"));
        roomFurniture.setStatus(true);
        when(roomFurnitureRepository.findByFurnitureId(Mockito.<Long>any())).thenReturn(roomFurniture);

        RoomImage roomImage = new RoomImage();
        roomImage.setRoomImage1("Room Image1");
        roomImage.setRoomImage2("Room Image2");
        roomImage.setRoomImage3("Room Image3");
        roomImage.setRoomImage4("Room Image4");
        roomImage.setRoomImageId(1L);
        when(roomImageRepository.findByRoomImageId(Mockito.<Long>any())).thenReturn(roomImage);

        Room room = new Room();
        room.setActivity(true);
        room.setDescription("The characteristics of someone or something");
        room.setDiscount(10.0f);
        room.setRoomCategoryId(1L);
        room.setRoomId(1L);
        room.setRoomImageId(1L);
        room.setServiceId(1L);
        room.setStatusId(1L);
        room.setViewCity("View City");

        ArrayList<Room> roomList = new ArrayList<>();
        roomList.add(room);
        when(roomRepository.findByRoomCategoryId(Mockito.<Long>any())).thenReturn(roomList);
        when(serviceRepository.findAll()).thenReturn(new ArrayList<>());
        BookRoomByCategory actualRoom = roomImpl.getRoom(1L);
        assertEquals(1, actualRoom.getCategoryRoomFurnitures().size());
        assertTrue(actualRoom.getServices().isEmpty());
        assertEquals(1, actualRoom.getRooms().size());
        assertEquals(1, actualRoom.getRoomFurnitures().size());
        assertEquals(1, actualRoom.getImages().size());
        verify(categoryRoomFurnitureRepository).findByRoomCategoryId(Mockito.<Long>any());
        verify(roomFurnitureRepository).findByFurnitureId(Mockito.<Long>any());
        verify(roomImageRepository).findByRoomImageId(Mockito.<Long>any());
        verify(roomRepository).findByRoomCategoryId(Mockito.<Long>any());
        verify(serviceRepository).findAll();
    }
}

