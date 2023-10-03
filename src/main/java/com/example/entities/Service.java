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
@Table(name = "service")
public class Service {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_id")
	private Long serviceId;

	@Column(name = "service_name", nullable = false)
	private String serviceName;

	@Column(name = "service_price", nullable = false)
	private Long servicePrice;

	@Column(name = "service_des")
	private String serviceDes;

	@Column(name = "service_note")
	private String serviceNote;

	@Column(name = "status", nullable = false)
	private Boolean status;

	@OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
	private Set<RoomService> roomServices;

	@OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
	private Set<ServiceFeedback> serviceFeedbacks;

}
