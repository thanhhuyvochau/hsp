package fu.hbs.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fu.hbs.entities.Token;
import fu.hbs.entities.User;
import fu.hbs.repository.TokenRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TokenServiceImpl.class})
@ExtendWith(SpringExtension.class)
class TokenServiceImplTest {
    @MockBean(name = "tokenRepository")
    private TokenRepository tokenRepository;

    @Autowired
    private TokenServiceImpl tokenServiceImpl;

    /**
     * Method under test: {@link TokenServiceImpl#createToken(User)}
     * Test case này đang test hàm createToken của
     * tokenService khi người dùng đã có token cũ tồn tại trong csdl.
     */
    @Test
    public void testCreateTokenWhenTokenExistsThenReturnUpdatedToken() {
        //SETUP: Tạo một user và một token cũ cho user đó. Token cũ sẽ hết hạn sau 1 giờ.
        User user = new User();
        user = new User();
        user.setUserId(1L);

        Token token = new Token();
        token.setUserId(user.getUserId());
        token.setToken("oldToken");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        token.setExpirationDate(calendar.getTime());
        //Mock tokenRepository.findByUserId() trả về token cũ đã có sẵn.
        when(tokenRepository.findByUserId(user.getUserId())).thenReturn(token);

        //Mock tokenRepository.save() trả lại đối tượng được truyền vào để lưu token mới
        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

//Gọi hàm createToken của tokenService với tham số user truyền vào
        Token result = tokenServiceImpl.createToken(user);
        //Khác null
        assertNotNull(result);

        //Token mới khác với token cũ
        assertNotEquals("oldToken", result.getToken());

        //Ngày hết hạn mới lớn hơn hiện tại
        assertTrue(result.getExpirationDate().after(new Date()));
    }

    @Test
    public void testCreateTokenWhenTokenDoesNotExistThenReturnNewToken() {
        //SETUP: Tạo một user và một token cũ cho user đó. Token cũ sẽ hết hạn sau 1 giờ.
        User user = new User();
        user = new User();
        user.setUserId(1L);

        Token token = new Token();
        token.setUserId(user.getUserId());
        token.setToken("oldToken");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        token.setExpirationDate(calendar.getTime());

        //Mock tokenRepository.findByUserId() trả về null, tức là chưa có token cho user
        when(tokenRepository.findByUserId(user.getUserId())).thenReturn(null);

        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Token result = tokenServiceImpl.createToken(user);

        assertNotNull(result);
        //UserId của token trả về bằng userId truyền vào
        assertEquals(user.getUserId(), result.getUserId());
        assertTrue(result.getExpirationDate().after(new Date()));
    }
/**
 * Test case này nhằm kiểm tra hành vi khi xảy ra ngoại lệ trong quá trình tạo token:
 */
    @Test
    public void testCreateTokenWhenExceptionOccursThenThrowException() {
        //SETUP: Tạo một user và một token cũ cho user đó. Token cũ sẽ hết hạn sau 1 giờ.
        User user = new User();
        user = new User();
        user.setUserId(1L);

        Token token = new Token();
        token.setUserId(user.getUserId());
        token.setToken("oldToken");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        token.setExpirationDate(calendar.getTime());
//Mock đối tượng tokenRepository, khi findByUserId sẽ ném ra một RuntimeException
        when(tokenRepository.findByUserId(user.getUserId())).thenThrow(new RuntimeException());
//Sử dụng assertThrows để kiểm tra xem hàm createToken có ném ra RuntimeException không khi được gọi
//Nếu có ngoại lệ được ném ra thì test pass, ngược lại sẽ fail
        User finalUser = user;
        assertThrows(RuntimeException.class, () -> tokenServiceImpl.createToken(finalUser));
    }


    /**
     * Method under test: {@link TokenServiceImpl#findTokenByValue(String)}
     */
    @Test
    void testFindTokenByValue() {
        Token token = mock(Token.class);
        when(token.isExpired()).thenReturn(false);
        doNothing().when(token).setExpirationDate(Mockito.<Date>any());
        doNothing().when(token).setId(Mockito.<Long>any());
        doNothing().when(token).setToken(Mockito.<String>any());
        doNothing().when(token).setUserId(Mockito.<Long>any());
        token.setExpirationDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        token.setId(1L);
        token.setToken("ABC123");
        token.setUserId(1L);
        when(tokenRepository.findByToken(Mockito.<String>any())).thenReturn(token);
        tokenServiceImpl.findTokenByValue("ABC123");
        verify(tokenRepository).findByToken(Mockito.<String>any());
        verify(token).isExpired();
        verify(token).setExpirationDate(Mockito.<Date>any());
        verify(token).setId(Mockito.<Long>any());
        verify(token).setToken(Mockito.<String>any());
        verify(token).setUserId(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link TokenServiceImpl#findTokenByValue(String)}
     *     Test case này giả lập trường hợp khi được truyền vào
     *     một token đã hết hạn thì hàm findTokenByValue sẽ trả về null.
     */

    @Test
    public void testFindTokenByValueWhenExpiredTokenThenReturnNull() {
        User user = new User();
        user.setUserId(1L);
        Token token = new Token();
        token.setUserId(user.getUserId());
        token.setToken("valid-token");
        //set thời gian hết hạn của token bằng cách trừ 1 giờ so với thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        token.setExpirationDate(calendar.getTime());
        // token hết hạn
        when(tokenRepository.findByToken("expired-token")).thenReturn(token);


        //Gọi phương thức findTokenByValue của tokenService, truyền vào token đã hết hạn.
        Token result = tokenServiceImpl.findTokenByValue("expired-token");


        //Kiểm tra kết quả trả về là null, để đảm bảo rằng khi token hết hạn thì hàm sẽ trả về null
        assertNull(result);
    }



    @Test
    public void testFindTokenByValueWhenInvalidTokenThenReturnNull() {
        when(tokenRepository.findByToken("invalid-token")).thenReturn(null);

        Token result = tokenServiceImpl.findTokenByValue("invalid-token");

        assertNull(result);
    }



    /**
     * Method under test: {@link TokenServiceImpl#deleteToken(Long)}
     */
    @Test
    void testDeleteToken() {
        doNothing().when(tokenRepository).deleteById(Mockito.<Long>any());
        tokenServiceImpl.deleteToken(1L);
        verify(tokenRepository).deleteById(Mockito.<Long>any());
    }
}

