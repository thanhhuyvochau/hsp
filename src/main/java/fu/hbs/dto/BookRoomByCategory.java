package fu.hbs.dto;

import java.util.List;

import fu.hbs.entities.CategoryRoomFurniture;
import fu.hbs.entities.Room;
import fu.hbs.entities.RoomFurniture;
import fu.hbs.entities.RoomImage;
import fu.hbs.entities.RoomService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookRoomByCategory {
	private List<Room> rooms;
	private List<CategoryRoomFurniture> categoryRoomFurnitures;
	private List<RoomService> services;
	private List<RoomImage> images;
	private List<RoomFurniture> roomFurnitures;
}
