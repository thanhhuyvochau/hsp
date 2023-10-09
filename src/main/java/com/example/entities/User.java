package com.example.entities;

import java.sql.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "dob", nullable = true)
	private Date dob;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "address", nullable = true)
	private String address;

	@Column(name = "phone", unique = true, nullable = true)
	private String phone;

	@Column(name = "status", nullable = true)
	private Boolean status;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<UserRole> userRoles;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<ServiceFeedback> serviceFeedbacks;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<New> news;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Notification> notifications;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<RoomFeedback> roomFeedbacks;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Reservation> reservations;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Invoice> invoices;
}
