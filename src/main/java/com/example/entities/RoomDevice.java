package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "room_device")
public class RoomDevice {
	@Id
	@ManyToOne
	@JoinColumn(name = "device_id")
	private Device device;

	@Id
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "status")
	private String status;

	@Column(name = "note")
	private String note;

}
