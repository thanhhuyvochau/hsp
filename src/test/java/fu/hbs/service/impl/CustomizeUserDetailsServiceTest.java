package fu.hbs.service.impl;

import fu.hbs.entities.Role;
import fu.hbs.entities.Token;
import fu.hbs.entities.User;
import fu.hbs.repositoties.RoleRepository;
import fu.hbs.repositoties.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomizeUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private CustomizeUserDetailsService customizeUserDetailsService;

    private User user;
    private Role role;
    //Trong phương thức setUp(), ta khởi tạo đối tượng user và role với các giá trị đầu vào hợp lệ
    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@gmail.com");
        user.setUserId(1L);
        user.setPassword("password");
        role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ROLE_USER");
        role.setStatus(true);
    }
    
//Test case này nhằm kiểm tra phương thức loadUserByUsername của lớp UserDetailsService khi truyền vào email hợp lệ
    @Test
    @DisplayName("Test 'loadUserByUsername' method with valid email")
    public void testLoadUserByUsernameWhenValidEmailThenReturnUserDetails() {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
//Sử dụng mock cho các phương thức truy vấn dữ liệu từ cơ sở dữ liệu: 
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(roleRepository.findRole(user.getUserId())).thenReturn(roles);
        
//Gọi phương thức loadUserByUsername(), truyền vào giá trị email hợp lệ là test@gmail.com
        UserDetails userDetails = customizeUserDetailsService.loadUserByUsername(user.getEmail());
        
// /Email trả về phải bằng email đã khởi tạo
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role.getRoleName())));

    }
  
    @Test
    @DisplayName("Test 'loadUserByUsername' method with valid password")
    public void testLoadUserByUsernameWhenValidPasswordThenReturnUserDetails() {
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        when(userRepository.findByEmail(user.getPassword())).thenReturn(user);
        when(roleRepository.findRole(user.getUserId())).thenReturn(roles);

        UserDetails userDetails = customizeUserDetailsService.loadUserByUsername(user.getPassword());

        assertEquals(user.getEmail(), userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role.getRoleName())));

    }

    @Test
    @DisplayName("Test 'loadUserByUsername' method with invalid email")
    public void testLoadUserByUsernameWhenInvalidEmailThenThrowException() {

//Sử dụng mock cho hàm findByEmail trong userRepository để trả về null 
//khi tìm user theo email. Đây là điều kiện để hàm loadUserByUsername sau đó sẽ ném exception.
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> customizeUserDetailsService.loadUserByUsername(user.getEmail()));
    }



    @Test
    @DisplayName("Test 'loadUserByUsername' method with invalid Password")
    public void testLoadUserByUsernameWhenInvalidPasswordThenThrowException() {
        User user1;
        user1 = new User();
        user1.setUserId(1L);
        user1.setEmail("test@gmail.com");
        user1.setPassword("");

        role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ROLE_USER");
        role.setStatus(true);
        when(userRepository.findByEmail(user.getPassword())).thenReturn(user1);

        assertThrows(PotentialStubbingProblem.class, () -> customizeUserDetailsService.loadUserByUsername(user1.getPassword()));
    }
}