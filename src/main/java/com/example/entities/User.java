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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor

@Getter
@Setter
@ToString
@Entity
@Table(name = "User")
public class User {

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "EMAIL", nullable = false, unique = true)
	private String email;

	@Column(name = "PHONE", nullable = false, unique = true)
	private String phone;

	@Column(name = "ADDRESS", nullable = false)
	private String address;

	@Column(name = "ENCRYTED_PASSWORD", nullable = false)
	private String encryptedPassword;

	@Column(name = "ENABLED", columnDefinition = "BIT")
	private int enabled;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<UserRole> userRoles;
}
