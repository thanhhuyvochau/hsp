package fu.hbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fu.hbs.dto.UserDto;
import fu.hbs.entities.User;
import fu.hbs.entities.UserRole;
import fu.hbs.exceptionHandler.UserIvalidException;
import fu.hbs.exceptionHandler.UserNotFoundException;
import fu.hbs.repository.UserRepository;
import fu.hbs.repository.UserRoleRepository;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean(name = "userRepository")
    private UserRepository userRepository;

    @MockBean(name = "userroleReposity")
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#save(UserDto)}
     */
    @Test
    void testSave() {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

        UserRole userRole = new UserRole();
        userRole.setId(1L);
        userRole.setRoleId(1L);
        userRole.setUserId(1L);
        when(userRoleRepository.save(Mockito.<UserRole>any())).thenReturn(userRole);
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        assertSame(user, userServiceImpl.save(new UserDto("janedoe", "jane.doe@example.org", "iloveyou")));
        verify(userRepository).save(Mockito.<User>any());
        verify(userRoleRepository).save(Mockito.<UserRole>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#save(User)}
     */
    @Test
    void testSave2() {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setDob(mock(Date.class));
        user2.setEmail("jane.doe@example.org");
        user2.setGender("Gender");
        user2.setImage("Image");
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhone("6625550144");
        user2.setStatus(true);
        user2.setUserId(1L);
        assertSame(user, userServiceImpl.save(user2));
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#checkPasswordUser(String, String)}
     *  Test case này đang kiểm tra hàm checkPasswordUser
     *     của userService khi mật khẩu của người dùng đúng.
     */
    @Test
    @DisplayName("Test the 'checkPasswordUser' method when the user's password is correct")
    public void testCheckPasswordUserWhenPasswordIsCorrectThenReturnTrue() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        //Sử dụng Mockito để mock trả về đối tượng user khi gọi hàm findByEmail của userRepository
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        //Gọi hàm checkPasswordUser của userService truyền email và password đã có sẵn của user
        boolean result = userServiceImpl.checkPasswordUser(user.getEmail(), user.getPassword());

        //Kiểm tra kết quả trả về của hàm checkPasswordUser là true khi password đúng
        assertTrue(result);

    }


    /**
     * Method under test: {@link UserServiceImpl#checkPasswordUser(String, String)}
     */
    @Test
    @DisplayName("Test the 'checkPasswordUser' method when the user's password is incorrect")
    public void testCheckPasswordUserWhenPasswordIsIncorrectThenReturnFalse() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        boolean result = userServiceImpl.checkPasswordUser(user.getEmail(), "wrongPassword");

        assertFalse(result);

    }


    @Test
    @DisplayName("Test the 'checkPasswordUser' method when the user does not exist")
    public void testCheckPasswordUserWhenUserDoesNotExistThenReturnFalse() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        User user1;
        user1 = new User();
        user1.setEmail("test@gmail.com");
        user1.setPassword("password12");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user1);

        boolean result = userServiceImpl.checkPasswordUser(user.getEmail(), user.getPassword());

        assertFalse(result);
    }

    /**
     * Method under test: {@link UserServiceImpl#checkUserbyEmail(String)}
     */
    @Test
    void testCheckUserbyEmail() {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(user);
        assertTrue(userServiceImpl.checkUserbyEmail("jane.doe@example.org"));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserbyEmail(String)}
     */
    @Test
    void testGetUserbyEmail() {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        when(userRepository.getUserByEmail(Mockito.<String>any())).thenReturn(user);
        assertSame(user, userServiceImpl.getUserbyEmail("jane.doe@example.org"));
        verify(userRepository).getUserByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#update(User)}
     */
    @Test
    void testUpdate() throws UserIvalidException {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setDob(mock(Date.class));
        user2.setEmail("jane.doe@example.org");
        user2.setGender("Gender");
        user2.setImage("Image");
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhone("6625550144");
        user2.setStatus(true);
        user2.setUserId(1L);
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        when(userRepository.findByPhone(Mockito.<String>any())).thenReturn(ofResult);

        User user3 = new User();
        user3.setAddress("42 Main St");
        user3.setDob(mock(Date.class));
        user3.setEmail("jane.doe@example.org");
        user3.setGender("Gender");
        user3.setImage("Image");
        user3.setName("Name");
        user3.setPassword("iloveyou");
        user3.setPhone("6625550144");
        user3.setStatus(true);
        user3.setUserId(1L);
        assertSame(user, userServiceImpl.update(user3));
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByPhone(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#update(User)}
     */
    @Test
    void testUpdate2() throws UserIvalidException {
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(-1L);
        doNothing().when(user).setAddress(Mockito.<String>any());
        doNothing().when(user).setDob(Mockito.<Date>any());
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setGender(Mockito.<String>any());
        doNothing().when(user).setImage(Mockito.<String>any());
        doNothing().when(user).setName(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setPhone(Mockito.<String>any());
        doNothing().when(user).setStatus(anyBoolean());
        doNothing().when(user).setUserId(Mockito.<Long>any());
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByPhone(Mockito.<String>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setDob(mock(Date.class));
        user2.setEmail("jane.doe@example.org");
        user2.setGender("Gender");
        user2.setImage("Image");
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhone("6625550144");
        user2.setStatus(true);
        user2.setUserId(1L);
        assertThrows(UserIvalidException.class, () -> userServiceImpl.update(user2));
        verify(userRepository).findByPhone(Mockito.<String>any());
        verify(user).getUserId();
        verify(user).setAddress(Mockito.<String>any());
        verify(user).setDob(Mockito.<Date>any());
        verify(user).setEmail(Mockito.<String>any());
        verify(user).setGender(Mockito.<String>any());
        verify(user).setImage(Mockito.<String>any());
        verify(user).setName(Mockito.<String>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(user).setPhone(Mockito.<String>any());
        verify(user).setStatus(anyBoolean());
        verify(user).setUserId(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#update(User)}
     */
    @Test
    void testUpdate3() throws UserIvalidException {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByPhone(Mockito.<String>any())).thenReturn(emptyResult);

        User user2 = new User();
        user2.setAddress("42 Main St");
        user2.setDob(mock(Date.class));
        user2.setEmail("jane.doe@example.org");
        user2.setGender("Gender");
        user2.setImage("Image");
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhone("6625550144");
        user2.setStatus(true);
        user2.setUserId(1L);
        assertSame(user, userServiceImpl.update(user2));
        verify(userRepository).save(Mockito.<User>any());
        verify(userRepository).findByPhone(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findById(Long)}
     */
    @Test
    void testFindById() throws UserNotFoundException {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertSame(user, userServiceImpl.findById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findById(Long)}
     */
    @Test
    void testFindById2() throws UserNotFoundException {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.findById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByPhone(String)}
     */
    @Test
    void testFindByPhone() throws UserNotFoundException {
        User user = new User();
        user.setAddress("42 Main St");
        user.setDob(mock(Date.class));
        user.setEmail("jane.doe@example.org");
        user.setGender("Gender");
        user.setImage("Image");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhone("6625550144");
        user.setStatus(true);
        user.setUserId(1L);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByPhone(Mockito.<String>any())).thenReturn(ofResult);
        assertTrue(userServiceImpl.findByPhone("6625550144"));
        verify(userRepository).findByPhone(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#findByPhone(String)}
     */
    @Test
    void testFindByPhone2() throws UserNotFoundException {
        when(userRepository.findByPhone(Mockito.<String>any())).thenReturn(null);
        assertFalse(userServiceImpl.findByPhone("6625550144"));
        verify(userRepository).findByPhone(Mockito.<String>any());
    }
}

