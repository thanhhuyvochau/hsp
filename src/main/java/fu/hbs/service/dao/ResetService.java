/*
 * Copyright (C) 2023, FPT University 
 * SEP490 - SEP490_G77
 * HBS 
 * Hotel Booking System 
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 27/10/2023    1.0        HieuLBM          First Deploy	
 */
package fu.hbs.service.dao;

import fu.hbs.entities.Token;
import fu.hbs.exceptionHandler.MailExceptionHandler;
import fu.hbs.exceptionHandler.ResetExceptionHandler;

public interface ResetService {
	void resetPasswordRequest(String email) throws ResetExceptionHandler;

	boolean resetPassword(Token token, String newPassword) throws ResetExceptionHandler;

	void sendResetPasswordEmail(String recipientEmail, String subject, String emailContent) throws MailExceptionHandler;
}
