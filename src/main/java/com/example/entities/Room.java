package com.example.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "room")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Long roomId;

	@ManyToOne
	@JoinColumn(name = "room_image_id", nullable = false)
	private RoomImage roomImage;

	@ManyToOne
	@JoinColumn(name = "room_category_id", nullable = false)
	private RoomCategories roomCategory;

	@Column(name = "description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "status_id", nullable = false)
	private RoomStatus status;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private Set<RoomDevice> roomDevices;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private Set<RoomService> roomServices;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private Set<RoomFeedback> roomFeedbacks;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private Set<Reservation> reservations;

}
