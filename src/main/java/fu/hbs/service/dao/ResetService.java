package fu.hbs.service.dao;

import fu.hbs.entities.Token;
import fu.hbs.exceptionHandler.MailExceptionHandler;
import fu.hbs.exceptionHandler.ResetExceptionHandler;

public interface ResetService {
	void resetPasswordRequest(String email) throws ResetExceptionHandler;

	boolean resetPassword(Token token, String newPassword) throws ResetExceptionHandler;

	void sendResetPasswordEmail(String recipientEmail, String subject, String emailContent) throws MailExceptionHandler;
}
