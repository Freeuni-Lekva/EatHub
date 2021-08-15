package ge.eathub.service.impl;

import ge.eathub.dao.impl.InMemoryUserDao;
import ge.eathub.dto.UserDto;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.InvalidEmailException;
import ge.eathub.exceptions.InvalidUserPasswordException;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.exceptions.UserNotFoundException;
import ge.eathub.mailer.Mailer;
import ge.eathub.models.User;
import ge.eathub.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    InMemoryUserDao daoMockito;

    @Test
    public void registerUserTest1() {
        UserService userService = new UserServiceImpl(daoMockito);
        when(daoMockito.createUser(any())).thenReturn(new User());
        when(daoMockito.checkInfo(any(), any())).thenReturn(true);
        try (MockedStatic<Mailer> mailer = Mockito.mockStatic(Mailer.class)) {
            mailer.when(() -> Mailer.sendMail(any())).thenReturn(true);
            userService.registerUser(new UserRegisterDto("asd", "asd", "asd@asd.com"));
        }
    }

    @Test
    public void registerUserException() {
        UserService userService = new UserServiceImpl(daoMockito);
        when(daoMockito.checkInfo(any(), any())).thenReturn(true);
        when(daoMockito.createUser(any())).thenThrow(UserCreationException.class);
        try (MockedStatic<Mailer> mailer = Mockito.mockStatic(Mailer.class)) {
            mailer.when(() -> Mailer.sendMail(any())).thenReturn(true);
            assertThrows(UserCreationException.class, () -> userService.registerUser(new UserRegisterDto("asd", "asd", "asd@asd.com")));
        }
    }

    @Test
    public void registerUserEmail() {
        UserService userService = new UserServiceImpl(daoMockito);
        try (MockedStatic<Mailer> mailer = Mockito.mockStatic(Mailer.class)) {
            mailer.when(() -> Mailer.sendMail(any())).thenReturn(true);
            assertThrows(InvalidEmailException.class, () -> userService.registerUser(new UserRegisterDto("asd", "asd", "asdasd")));
        }
    }

    @Test
    void loginTest1() {
        String username = "asd";
        String email = "asd@asd.com";
        String password = "asd";
        UserService userService = new UserServiceImpl(new InMemoryUserDao());
        try (MockedStatic<Mailer> mailer = Mockito.mockStatic(Mailer.class)) {
            mailer.when(() -> Mailer.sendMail(any())).thenReturn(true);
            userService.registerUser(new UserRegisterDto(username, password, email));
//        when(daoMockito.getUserByUsername(username)).thenReturn(Optional.of(new User(username,email)))
            try {
                UserDto userDto = userService.loginUser(new UserLoginDto(username, password));
                assertEquals(username, userDto.getUsername());
            } catch (InvalidUserPasswordException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void loginTest2() {
        String username = "asd";
        String email = "asd@asd.com";
        String password = "asd";
        UserService userService = new UserServiceImpl(daoMockito);
        try (MockedStatic<Mailer> mailer = Mockito.mockStatic(Mailer.class)) {
            mailer.when(() -> Mailer.sendMail(any())).thenReturn(true);
//        userService.registerUser(new UserRegisterDto(username, password,email));
            when(daoMockito.getUserByUsername(username)).thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class, () -> userService.loginUser(new UserLoginDto(username, password)));
        }
    }

    @Test
    void loginTest3() {
        String username = "asd";
        String email = "asd@asd.com";
        String password = "asd";
        UserService userService = new UserServiceImpl(new InMemoryUserDao());
        try (MockedStatic<Mailer> mailer = Mockito.mockStatic(Mailer.class)) {
            mailer.when(() -> Mailer.sendMail(any())).thenReturn(true);
            userService.registerUser(new UserRegisterDto(username, password, email));
            assertThrows(InvalidUserPasswordException.class, () -> userService.loginUser(new UserLoginDto(username, "qsadsdasd")));
        }
    }
}