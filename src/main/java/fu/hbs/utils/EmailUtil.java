package fu.hbs.utils;

import fu.hbs.exceptionHandler.MailExceptionHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    private static JavaMailSender staticJavaMailSender;

    EmailUtil(JavaMailSender javaMailSender) {
        staticJavaMailSender = javaMailSender;
    }

    public static void sendBookingEmail(String recipientEmail, String subject, String emailContent)
            throws MailExceptionHandler {
        MimeMessage message = staticJavaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("3HKT@gmail.com");
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(emailContent, true); // Sử dụng HTML

            staticJavaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailExceptionHandler("Lỗi gửi mail");
        }
    }
}
