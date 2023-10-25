package fu.hbs.service.impl;

import fu.hbs.entities.Token;
import fu.hbs.repositoties.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTestDeleteToken {

	@Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private Long tokenId;

    @BeforeEach
    public void setUp() {
        tokenId = 1L;
    }

    
    //Test case này kiểm tra chức năng xóa token khi token đã tồn tại 
    @Test
    public void testDeleteTokenWhenTokenExistsThenTokenIsDeleted() {
    	
        // Arrange
    	//Tạo đối tượng Token, set id = tokenId đã được khởi tạo ở trước
        Token token = new Token();
        token.setId(tokenId);


        // Act
        //Gọi phương thức deleteToken của tokenService, truyền vào tham số là tokenId
        tokenService.deleteToken(tokenId);

        // Assert
        //Sử dụng verify để kiểm tra xem phương thức deleteById của tokenRepository có được gọi 1 lần với tham số là tokenId hay không.
        verify(tokenRepository, times(1)).deleteById(tokenId);
    }


    @Test
    public void testDeleteTokenWhenTokenDoesNotExistThenTokenIsNotDeleted() {
        // Arrange
        when(tokenRepository.findById(tokenId)).thenReturn(Optional.empty());

        // Act
        tokenService.deleteToken(tokenId);

        // Assert
        verify(tokenRepository, never()).deleteById(tokenId);
    }
}