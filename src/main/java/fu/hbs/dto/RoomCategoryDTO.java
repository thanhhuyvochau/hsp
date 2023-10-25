package fu.hbs.dto;

import fu.hbs.entities.CategoryRoomPrice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomCategoryDTO {
	private Long roomCategoryId;
	private String roomCategoryName;
	private CategoryRoomPrice categoryRoomPrice;
	private String description;
	private String image;
}
