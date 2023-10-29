package fu.hbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fu.hbs.entities.Token;
import fu.hbs.entities.User;
import fu.hbs.exceptionHandler.MailExceptionHandler;
import fu.hbs.exceptionHandler.ResetExceptionHandler;
import fu.hbs.repository.TokenRepository;
import fu.hbs.repository.UserRepository;
import fu.hbs.service.dao.TokenService;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

@ContextConfiguration(classes = {RestPasswordImpl.class})
@ExtendWith(SpringExtension.class)
class RestPasswordImplTest {
    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestPasswordImpl restPasswordImpl;

    @MockBean
    private TemplateEngine templateEngine;

    @MockBean(name = "tokenRepository")
    private TokenRepository tokenRepository;

    @MockBean
    private TokenService tokenService;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    /**
     * Method under test: {@link RestPasswordImpl#resetPasswordRequest(String)}
     */
    @Test
    void testResetPasswordRequest() throws ResetExceptionHandler, MailException {
        when(templateEngine.process(Mockito.<String>any(), Mockito.<IContext>any())).thenReturn("Process");

        User user = new User();
        user.setDob(mock(java.sql.Date.class));
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setUserId(1L);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(user);

        Token token = new Token();

        token.setId(1L);
        token.setToken("ABC123");
        token.setUserId(1L);
        when(tokenService.createToken(Mockito.<User>any())).thenReturn(token);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        restPasswordImpl.resetPasswordRequest("jane.doe@example.org");
        verify(templateEngine).process(Mockito.<String>any(), Mockito.<IContext>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(tokenService).createToken(Mockito.<User>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link RestPasswordImpl#resetPasswordRequest(String)}
     */
    @Test
    void testResetPasswordRequest2() throws ResetExceptionHandler, MailException {
        when(templateEngine.process(Mockito.<String>any(), Mockito.<IContext>any())).thenReturn("Process");

        User user = new User();
        user.setDob(mock(java.sql.Date.class));
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setUserId(1L);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(user);

        Token token = new Token();

        token.setId(1L);
        token.setToken("ABC123");
        token.setUserId(1L);
        when(tokenService.createToken(Mockito.<User>any())).thenReturn(token);
        doThrow(new MailExceptionHandler("An error occurred")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertThrows(MailExceptionHandler.class, () -> restPasswordImpl.resetPasswordRequest("jane.doe@example.org"));
        verify(templateEngine).process(Mockito.<String>any(), Mockito.<IContext>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(tokenService).createToken(Mockito.<User>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link RestPasswordImpl#resetPasswordRequest(String)}
     */
    @Test
    void testResetPasswordRequest3() throws ResetExceptionHandler, MailException {
        when(templateEngine.process(Mockito.<String>any(), Mockito.<IContext>any())).thenReturn("Process");
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("foo");
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setUserId(Mockito.<Long>any());

        user.setDob(mock(java.sql.Date.class));
        user.setEmail("jane.doe@example.org");

        user.setPassword("iloveyou");
        user.setUserId(1L);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(user);

        Token token = new Token();

        token.setId(1L);
        token.setToken("ABC123");
        token.setUserId(1L);
        when(tokenService.createToken(Mockito.<User>any())).thenReturn(token);
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        restPasswordImpl.resetPasswordRequest("jane.doe@example.org");
        verify(templateEngine).process(Mockito.<String>any(), Mockito.<IContext>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(user).getEmail();
        verify(user).setEmail(Mockito.<String>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(user).setUserId(Mockito.<Long>any());
        verify(tokenService).createToken(Mockito.<User>any());
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link RestPasswordImpl#resetPasswordRequest(String)}
     */
    @Test
    void testResetPasswordRequest4() throws ResetExceptionHandler {
        when(templateEngine.process(Mockito.<String>any(), Mockito.<IContext>any())).thenReturn("Process");
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("");
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setUserId(Mockito.<Long>any());
        user.setDob(mock(java.sql.Date.class));
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setStatus(true);
        user.setUserId(1L);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(user);

        Token token = new Token();
        token.setId(1L);
        token.setToken("ABC123");
        token.setUserId(1L);
        when(tokenService.createToken(Mockito.<User>any())).thenReturn(token);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertThrows(MailExceptionHandler.class, () -> restPasswordImpl.resetPasswordRequest("jane.doe@example.org"));
        verify(templateEngine).process(Mockito.<String>any(), Mockito.<IContext>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(user).getEmail();
        verify(user).setEmail(Mockito.<String>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(tokenService).createToken(Mockito.<User>any());
        verify(javaMailSender).createMimeMessage();
    }

    /**
     * Method under test: {@link RestPasswordImpl#resetPassword(Token, String)}
     */
    @Test
    void testResetPassword() throws ResetExceptionHandler {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(java.sql.Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        Optional<User> ofResult = Optional.of(user);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setDob(mock(java.sql.Date.class));
        user2.setEmail("jane.doe@example.org");
        user2.setGender("Gender");
        user2.setImage("Image");
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhone("6625550144");
        user2.setStatus(true);
        user2.setUserId(1L);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(tokenRepository).deleteById(Mockito.<Long>any());
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        Token token = new Token();
        token.setExpirationDate(
                java.util.Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        token.setId(1L);
        token.setToken("ABC123");
        token.setUserId(1L);
        assertTrue(restPasswordImpl.resetPassword(token, "iloveyou"));
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(tokenRepository).deleteById(Mockito.<Long>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }

    /**
     * Method under test: {@link RestPasswordImpl#resetPassword(Token, String)}
     */
    @Test
    void testResetPassword2() throws ResetExceptionHandler {
        when(passwordEncoder.encode(Mockito.<CharSequence>any()))
                .thenThrow(new MailExceptionHandler("An error occurred"));

        Token token = new Token();
        token.setExpirationDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        token.setId(1L);
        token.setToken("ABC123");
        token.setUserId(1L);
        assertThrows(MailExceptionHandler.class, () -> restPasswordImpl.resetPassword(token, "iloveyou"));
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }

    /**
     * Method under test: {@link RestPasswordImpl#resetPassword(Token, String)}
     */
    @Test
    void testResetPassword3() throws ResetExceptionHandler {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        Token token = new Token();
        token.setExpirationDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        token.setId(1L);
        token.setToken("ABC123");
        token.setUserId(1L);
        assertThrows(ResetExceptionHandler.class, () -> restPasswordImpl.resetPassword(token, "iloveyou"));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }

    /**
     * Method under test: {@link RestPasswordImpl#sendResetPasswordEmail(String, String, String)}
     */
    @Test
    void testSendResetPasswordEmail() throws MailExceptionHandler, MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        restPasswordImpl.sendResetPasswordEmail("jane.doe@example.org", "Hello from the Dreaming Spires",
                "jane.doe@example.org");
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link RestPasswordImpl#sendResetPasswordEmail(String, String, String)}
     */
    @Test
    void testSendResetPasswordEmail2() throws MailExceptionHandler, MailException {
        doThrow(new MailExceptionHandler("An error occurred")).when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        assertThrows(MailExceptionHandler.class, () -> restPasswordImpl.sendResetPasswordEmail("jane.doe@example.org",
                "Hello from the Dreaming Spires", "jane.doe@example.org"));
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }
}

