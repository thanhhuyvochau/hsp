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
@Table(name = "device")

public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "device_id")
	private Long deviceId;

	@Column(name = "device_name", nullable = false)
	private String deviceName;

	@Column(name = "device_price", nullable = false)
	private double devicePrice;

	@Column(name = "status")
	private Boolean status;

	@OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
	private Set<RoomDevice> roomDevices;

}
