package fu.hbs.service.impl;

import fu.hbs.entities.Token;
import fu.hbs.entities.User;
import fu.hbs.repository.TokenRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTestCreateToken {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private User user;
    private Token token;

    
    //SETUP: Tạo một user và một token cũ cho user đó. Token cũ sẽ hết hạn sau 1 giờ.
    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1L);

        token = new Token();
        token.setUserId(user.getUserId());
        token.setToken("oldToken");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        token.setExpirationDate(calendar.getTime());
    }

  //Test case này đang test hàm createToken của tokenService khi người dùng đã có token cũ tồn tại trong csdl.
    @Test
    public void testCreateTokenWhenTokenExistsThenReturnUpdatedToken() {
  //Mock tokenRepository.findByUserId() trả về token cũ đã có sẵn.
        when(tokenRepository.findByUserId(user.getUserId())).thenReturn(token);
        
 //Mock tokenRepository.save() trả lại đối tượng được truyền vào để lưu token mới
        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
//Gọi hàm createToken của tokenService với tham số user truyền vào
        Token result = tokenService.createToken(user);
       //Khác null
        assertNotNull(result);
        
        //Token mới khác với token cũ
        assertNotEquals("oldToken", result.getToken());
        
        //Ngày hết hạn mới lớn hơn hiện tại
        assertTrue(result.getExpirationDate().after(new Date()));
    }

    @Test
    public void testCreateTokenWhenTokenDoesNotExistThenReturnNewToken() {
    	
  //Mock tokenRepository.findByUserId() trả về null, tức là chưa có token cho user
        when(tokenRepository.findByUserId(user.getUserId())).thenReturn(null);
        
        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Token result = tokenService.createToken(user);

        assertNotNull(result);
        
        //UserId của token trả về bằng userId truyền vào
        assertEquals(user.getUserId(), result.getUserId());
        assertTrue(result.getExpirationDate().after(new Date()));
    }

    
//Test case này nhằm kiểm tra hành vi khi xảy ra ngoại lệ trong quá trình tạo token:    
    @Test
    public void testCreateTokenWhenExceptionOccursThenThrowException() {
//Mock đối tượng tokenRepository, khi findByUserId sẽ ném ra một RuntimeException    	
        when(tokenRepository.findByUserId(user.getUserId())).thenThrow(new RuntimeException());
//Sử dụng assertThrows để kiểm tra xem hàm createToken có ném ra RuntimeException không khi được gọi
//Nếu có ngoại lệ được ném ra thì test pass, ngược lại sẽ fail        
        assertThrows(RuntimeException.class, () -> tokenService.createToken(user));
    }
}