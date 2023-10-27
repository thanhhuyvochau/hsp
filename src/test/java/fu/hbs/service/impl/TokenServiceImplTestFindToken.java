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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTestFindToken {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private Token token;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(1L);
//Tạo ra đối tượng Token, set userId bằng userId của User, set token là "valid-token", set hạn sử dụng sau 1 giờ
        token = new Token();
        token.setUserId(user.getUserId());
        token.setToken("valid-token");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        token.setExpirationDate(calendar.getTime());
    }
    
    
 
    @Test
    public void testFindTokenByValueWhenValidTokenThenReturnToken() {
    	
   //Sử dụng Mockito để mock hành vi khi gọi tokenRepository.findByToken("valid-token")
   //sẽ trả về đối tượng token đã tạo ở phần setup 	
        when(tokenRepository.findByToken("valid-token")).thenReturn(token);

        Token result = tokenService.findTokenByValue("valid-token");

   //Kiểm tra xem kết quả trả về có bằng đối tượng token không bằng cách so sánh 2 đối tượng bằng equals()
        assertEquals(token, result);
    }

    
   //Test case này giả lập trường hợp khi được truyền vào một token đã hết hạn thì hàm findTokenByValue sẽ trả về null.
    @Test
    public void testFindTokenByValueWhenExpiredTokenThenReturnNull() {
   //set thời gian hết hạn của token bằng cách trừ 1 giờ so với thời gian hiện tại 	
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1);
        token.setExpirationDate(calendar.getTime());

        when(tokenRepository.findByToken("token hết hạn")).thenReturn(token);
        
        //Gọi phương thức findTokenByValue của tokenService, truyền vào token đã hết hạn.
       Token result = tokenService.findTokenByValue("expired-token");

       
       //Kiểm tra kết quả trả về là null, để đảm bảo rằng khi token hết hạn thì hàm sẽ trả về null
        assertNull(result);
    }

    @Test
    public void testFindTokenByValueWhenInvalidTokenThenReturnNull() {
        when(tokenRepository.findByToken("invalid-token")).thenReturn(null);

        Token result = tokenService.findTokenByValue("invalid-token");

        assertNull(result);
    }
}