package com.example.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@ToString
public class UserDto {

	private int userId;
	private String userName;
	private Date userDob;
	private String userEmail;
	private String userPassword;
	private String userAddress;
	private String userPhone;

}
