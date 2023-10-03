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
@Table(name = "room_categories")
public class RoomCategories {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_category_id")
	private Long roomCategoryId;

	@Column(name = "room_category_name", nullable = false)
	private String roomCategoryName;

	@Column(name = "price", nullable = false)
	private double price;

	@Column(name = "description")
	private String description;

	@Column(name = "square", nullable = false)
	private Double square;

	@Column(name = "number_person", nullable = false)
	private Integer numberPerson;

	@OneToMany(mappedBy = "roomCategory", cascade = CascadeType.ALL)
	private Set<Room> rooms;

}
