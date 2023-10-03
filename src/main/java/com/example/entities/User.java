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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor

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

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "user_dob", nullable = false)
	private Date userDob;

	@Column(name = "user_email", unique = true, nullable = false)
	private String userEmail;

	@Column(name = "user_password", nullable = false)
	private String userPassword;

	@Column(name = "user_address", nullable = false)
	private String userAddress;

	@Column(name = "user_phone", unique = true, nullable = false)
	private String userPhone;

	@Column(name = "status", nullable = false)
	private Boolean status;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<UserRole> userRoles;

//	@ManyToMany
//	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//	private Set<User> users;

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
