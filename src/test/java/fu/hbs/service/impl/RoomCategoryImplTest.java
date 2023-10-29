package fu.hbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fu.hbs.dto.RoomCategoryDTO;
import fu.hbs.entities.CategoryRoomPrice;
import fu.hbs.entities.RoomCategories;
import fu.hbs.repository.CategoryRoomPriceRepository;
import fu.hbs.repository.RoomCategoriesRepository;

import java.math.BigDecimal;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {RoomCategoryImpl.class})
@ExtendWith(SpringExtension.class)
class RoomCategoryImplTest {
    @MockBean
    private CategoryRoomPriceRepository categoryRoomPriceRepository;

    @MockBean(name = "roomCategoriesRepository")
    private RoomCategoriesRepository roomCategoriesRepository;

    @Autowired
    private RoomCategoryImpl roomCategoryImpl;

    /**
     * Get a list of all room categories along with their details.
     *
     * @return List of RoomCategoryDTO containing room category information
     */


    /**
     * Method under test: {@link RoomCategoryImpl#getAllRoom()}
     */
    @Test
    void testGetAllRoom() {
        when(roomCategoriesRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(roomCategoryImpl.getAllRoom().isEmpty());
        verify(roomCategoriesRepository).findAll();
    }

    /**
     * Method under test: {@link RoomCategoryImpl#getAllRoom()}
     */
    @Test
    void testGetAllRoom2() {
        RoomCategories roomCategories = new RoomCategories();
        roomCategories.setDescription("The characteristics of someone or something");
        roomCategories.setImage("Image");
        roomCategories.setNumberPerson(10);
        roomCategories.setRoomCategoryId(1L);
        roomCategories.setRoomCategoryName("Room Category Name");
        roomCategories.setSquare(10.0d);

        ArrayList<RoomCategories> roomCategoriesList = new ArrayList<>();
        roomCategoriesList.add(roomCategories);
        when(roomCategoriesRepository.findAll()).thenReturn(roomCategoriesList);

        CategoryRoomPrice categoryRoomPrice = new CategoryRoomPrice();
        categoryRoomPrice.setCreatedDate(mock(Date.class));
        categoryRoomPrice.setEndDate(mock(Date.class));
        categoryRoomPrice.setPrice(new BigDecimal("2.3"));
        categoryRoomPrice.setRoomCategoryId(1L);
        categoryRoomPrice.setRoomPriceId(1L);
        categoryRoomPrice.setStartDate(mock(Date.class));
        categoryRoomPrice.setUpdatedDate(mock(Date.class));
        when(categoryRoomPriceRepository.findByRoomPriceId(Mockito.<Long>any())).thenReturn(categoryRoomPrice);
        List<RoomCategoryDTO> actualAllRoom = roomCategoryImpl.getAllRoom();
        assertEquals(1, actualAllRoom.size());
        RoomCategoryDTO getResult = actualAllRoom.get(0);
        assertSame(categoryRoomPrice, getResult.getCategoryRoomPrice());
        assertEquals("Room Category Name", getResult.getRoomCategoryName());
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals(1L, getResult.getRoomCategoryId().longValue());
        assertEquals("Image", getResult.getImage());
        verify(roomCategoriesRepository).findAll();
        verify(categoryRoomPriceRepository).findByRoomPriceId(Mockito.<Long>any());
    }
}

