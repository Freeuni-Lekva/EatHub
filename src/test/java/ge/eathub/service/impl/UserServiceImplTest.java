package ge.eathub.service.impl;

import ge.eathub.dao.UserDao;
import ge.eathub.dao.impl.InMemoryUserDao;
import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    InMemoryUserDao daoMockito;

    @Test
    public void registerUserTest1() {
        UserService userService = new UserServiceImpl(daoMockito);
        when(daoMockito.createUser(any())).thenReturn(null);
        assertTrue(userService.registerUser(new UserRegisterDto("asd", "asd", "asd@asd.com")));

    }

    @Test
    public void registerUserException() {
        UserService userService = new UserServiceImpl(daoMockito);
        when(daoMockito.createUser(any())).thenThrow(UserCreationException.class);
        assertFalse(userService.registerUser(new UserRegisterDto("asd", "asd", "asd@asd.com")));

    }

    @Test
    public void registerUserEmail() {
        UserService userService = new UserServiceImpl(daoMockito);
        assertFalse(userService.registerUser(new UserRegisterDto("asd", "asd", "asdasd")));

    }
}