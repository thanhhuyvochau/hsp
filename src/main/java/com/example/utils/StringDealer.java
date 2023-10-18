/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 18/10/2023    1.0        HieuLBM          First Deploy
 *  * 
 */
package com.example.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class StringDealer {
	public StringDealer() {
	}

	public String trimMax(String str) {
		if (str == null)
			return "";
		return str.trim().replaceAll("\\s+", " ");
	}

	public Date convertToDateAndFormat(String str) {
		Date date;
		try {
			date = new Date((new SimpleDateFormat("yyyy-MM-dd").parse(str)).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}

	public boolean checkEmailRegex(String email) {
		String regex = "^[a-zA-Z0-9!#$%&'*+/=?^`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
		return email.matches(regex);
	}

	public boolean checkPhoneRegex(String phone) {
		String regex = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$";
		return phone.matches(regex);
	}

	/**
	 * Check if password is valid or not. Ít nhất một chữ thường. Ít nhất một chữ
	 * cái viết hoa. Ít nhất một chữ số. Ít nhất một ký tự đặc biệt từ !, @, #,
	 * $hoặc %. Độ dài mật khẩu phải từ 8 đến 20 ký tự.
	 *
	 *
	 */

	public boolean checkPasswordRegex(String password) {
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,20}$";
		return password.matches(regex);
	}

	public long dateDiff(Date startDate, Date endDate) {
		long daysDiff = 0L;
		try {
			// calculating the difference b/w startDate and endDate

			long getDiff = endDate.getTime() - startDate.getTime();

			// using TimeUnit class from java.util.concurrent package
			daysDiff = TimeUnit.MILLISECONDS.toDays(getDiff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return daysDiff;
	}

	public boolean chechValidStreetString(String street) {
//        Pattern p = Pattern.compile("/[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+/");
//        String pattern = "/[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]/";
//        // boolean b = m.matches();
//        boolean check = pattern.matches(street);
		return false;
	}

}
