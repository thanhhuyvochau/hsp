package fu.hbs.dto;

import fu.hbs.entities.RoomCategories;
import fu.hbs.entities.RoomImage;
import fu.hbs.entities.RoomStatus;
import fu.hbs.entities.RoomService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomDTO {

	private Long roomId;
	private RoomImage roomImage;
	private RoomCategories roomCategories;
	private RoomStatus roomStatus;
	private RoomService service;
	private String description;
	private float discount;
	private String viewCity;
	private boolean activity;
}
