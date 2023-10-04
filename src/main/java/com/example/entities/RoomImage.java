package com.example.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "room_image")
public class RoomImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_image_id")
	private Long roomImageId;

	@Column(name = "room_image1", nullable = false)
	private String roomImage1;

	@Column(name = "room_image2", nullable = false)
	private String roomImage2;

	@Column(name = "room_image3", nullable = false)
	private String roomImage3;

	@Column(name = "room_image4", nullable = false)
	private String roomImage4;

	@OneToMany(mappedBy = "roomImage", cascade = CascadeType.ALL)
	private Set<Room> rooms;
}
