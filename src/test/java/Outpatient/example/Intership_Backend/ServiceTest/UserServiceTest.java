package Outpatient.example.Intership_Backend.ServiceTest;

import Outpatient.example.Intership_Backend.Advices.ApiError;
import Outpatient.example.Intership_Backend.DTO.LoginRequest;
import Outpatient.example.Intership_Backend.DTO.RegisterUserDTo;
import Outpatient.example.Intership_Backend.Entity.User;
import Outpatient.example.Intership_Backend.Repository.UserRepo;
import Outpatient.example.Intership_Backend.Service.DoctorService;
import Outpatient.example.Intership_Backend.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUsers() {
        userService.loadUsers();
        verify(userRepo, times(1)).saveAll(anyList());
    }

    @Test
    void testRegisterNewUser_Success() {
        RegisterUserDTo userDto = new RegisterUserDTo();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setConfirmPassword("password123");
        userDto.setRole("USER");

        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<ApiError> response = userService.registerNewUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registered Successfully", response.getBody().getMessage());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterNewUser_EmailExists() {
        RegisterUserDTo userDto = new RegisterUserDTo();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setConfirmPassword("password123");

        when(userRepo.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new User()));

        ResponseEntity<ApiError> response = userService.registerNewUser(userDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody().getMessage());
    }

    @Test
    void testRegisterNewUser_PasswordMismatch() {
        RegisterUserDTo userDto = new RegisterUserDTo();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setConfirmPassword("passwordMismatch");

        ResponseEntity<ApiError> response = userService.registerNewUser(userDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Passwords do not match", response.getBody().getMessage());
    }
//
//    @Test
//    void testAuthenticateUser_Success() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@example.com");
//        loginRequest.setPassword("password123");
//
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword("encodedPassword");
//        user.setRole("USER");
//
//        when(userRepo.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
//
//        ApiError response = userService.authenticateUser(loginRequest);
//
//        assertEquals(HttpStatus.OK, response.getStatus());
//        assertEquals("User login successful", response.getMessage());
//    }
//
//    @Test
//    void testAuthenticateUser_UserNotFound() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("notfound@example.com");
//        loginRequest.setPassword("password123");
//
//        when(userRepo.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());
//
//        ApiError response = userService.authenticateUser(loginRequest);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
//        assertEquals("User not found", response.getMessage());
//    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepo.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        ApiError response = userService.authenticateUser(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        assertEquals("Invalid password", response.getMessage());
    }

    @Test
    void testLoadUserByUsername_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepo.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("notfound@example.com")
        );
    }
}

