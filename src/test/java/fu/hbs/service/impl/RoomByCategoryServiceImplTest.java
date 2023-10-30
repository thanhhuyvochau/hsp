package fu.hbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fu.hbs.dto.BookRoomByCategory;
import fu.hbs.entities.CategoryRoomFurniture;
import fu.hbs.entities.Room;
import fu.hbs.repository.CategoryRoomFurnitureRepository;
import fu.hbs.repository.CategoryRoomPriceRepository;
import fu.hbs.repository.RoomCategoriesRepository;
import fu.hbs.repository.RoomFurnitureRepository;
import fu.hbs.repository.RoomImageRepository;
import fu.hbs.repository.RoomRepository;
import fu.hbs.repository.RoomServiceRepository;

@ExtendWith(MockitoExtension.class)
class RoomByCategoryServiceImplTest {
	@Mock
	RoomServiceRepository serviceRepository;
	@Mock
	RoomImageRepository roomImageRepository;
	@Mock
	RoomRepository roomRepository;
	@Mock
	RoomFurnitureRepository roomFurnitureRepository;
	@Mock
	CategoryRoomFurnitureRepository categoryRoomFurnitureRepository;
	@Mock
	RoomCategoriesRepository roomCategoriesRepository;
	@Mock
	CategoryRoomPriceRepository categoryRoomPriceRepository;

	@InjectMocks
	RoomByCategoryServiceImpl roomByCategoryServiceImpl;

	@Test
	void testGetRoom1() {
		List<fu.hbs.entities.RoomService> list = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		Room room = new Room();
		room.setRoomImageId(1L);
		room.setRoomCategoryId(1L);
		rooms.add(room);

		List<CategoryRoomFurniture> categoryRoomFurnitures = new ArrayList<CategoryRoomFurniture>();
		CategoryRoomFurniture categoryRoomFurniture = new CategoryRoomFurniture();
		categoryRoomFurniture.setNote("ook");
		categoryRoomFurniture.setFurnitureId(1L);
		categoryRoomFurnitures.add(categoryRoomFurniture);

		when(roomRepository.findByRoomCategoryId(Mockito.<Long>any())).thenReturn(rooms);
		when(categoryRoomFurnitureRepository.findByRoomCategoryId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
		when(serviceRepository.findAll()).thenReturn(new ArrayList<>());
		when(categoryRoomFurnitureRepository.findByRoomCategoryId(Mockito.<Long>any()))
				.thenReturn(categoryRoomFurnitures);

		BookRoomByCategory bookRoomByCategory = roomByCategoryServiceImpl.getRoom(1l);

		assertEquals(1, bookRoomByCategory.getRooms().size());
		assertEquals(1, bookRoomByCategory.getCategoryRoomFurnitures().size());

	}

}
