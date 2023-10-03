package com.example.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "new")
public class New {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "new_id")
	private Long newId;

	@Column(name = "new_image")
	private String newImage;

	@Column(name = "new_descriptions")
	private String newDescriptions;

	@Column(name = "new_date")
	private Date newDate;

	@Column(name = "new_title")
	private String newTitle;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
