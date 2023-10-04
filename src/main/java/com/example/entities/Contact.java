package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "contact")

public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contact_id")
	private Long contactId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_read")
	private Boolean isRead;

	@Column(name = "status", nullable = false)
	private Boolean status;
}
