package fu.hbs.controller;

import fu.hbs.controller.UserController;
import fu.hbs.dto.UserDto;
import fu.hbs.entities.User;
import fu.hbs.exceptionHandler.UserIvalidException;
import fu.hbs.exceptionHandler.UserNotFoundException;
import fu.hbs.service.dao.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testRegisterUserAccountWhenValidDataThenReturnSuccess() throws Exception {
        UserDto userDto = new UserDto("John Doe", "john.doe@gmail.com", "password");
        userDto.setCheckPass("password");

        
       // Sử dụng Mockito để mock phương thức checkUserbyEmail() của class UserService trả về false, tức người dùng chưa tồn tại trong CSDL.
        when(userService.checkUserbyEmail(userDto.getUserEmail())).thenReturn(false);
        
        //Gọi phương thức registerUserAccount() của UserController để đăng ký tài khoản
        String view = userController.registerUserAccount(userDto);
        
       //Kiểm tra kết quả trả về bằng phương thức assertEqual
        assertEquals("redirect:/registration?success", view);
    }

    @Test
    public void testRegisterUserAccountWhenEmailExistsThenReturnEmailexist() throws Exception {
        UserDto userDto = new UserDto("John Doe", "john.doe@gmail.com", "password");
        userDto.setCheckPass("password");

        when(userService.checkUserbyEmail(userDto.getUserEmail())).thenReturn(true);

        String view = userController.registerUserAccount(userDto);
   
        assertEquals("redirect:/registration?emailexist", view);
    }
 
    @Test
    public void testRegisterUserAccountWhenPasswordsDoNotMatchThenReturnCheckpass() throws Exception {
        UserDto userDto = new UserDto("John Doe", "john.doe@gmail.com", "password");
        userDto.setCheckPass("differentpassword");

        when(userService.checkUserbyEmail(userDto.getUserEmail())).thenReturn(false);

        String view = userController.registerUserAccount(userDto);

        assertEquals("redirect:/registration?checkpass", view);
    }
    
    
}