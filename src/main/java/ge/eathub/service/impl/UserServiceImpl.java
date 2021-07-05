package ge.eathub.service.impl;

import ge.eathub.dao.UserDao;
import ge.eathub.dto.UserDto;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.InvalidEmailException;
import ge.eathub.exceptions.InvalidUserPasswordException;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.exceptions.UserNotFoundException;
import ge.eathub.models.User;
import ge.eathub.service.UserService;
import ge.eathub.utils.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    private final static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    // TODO Send registration email
    @Override
    public void registerUser(UserRegisterDto userDto) throws UserCreationException, InvalidEmailException {
        logger.info("create user " + userDto.getUsername());
        if (!EmailValidator.validate(userDto.getEmail())) {
            throw new InvalidEmailException(userDto.getEmail());
        }
        User newUser = new User(userDto.getUsername(),
                BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()),
                userDto.getEmail());
        User createdUserNotUsed = userDao.createUser(newUser);

    }

    @Override
    public UserDto loginUser(UserLoginDto userDto) throws UserNotFoundException, InvalidUserPasswordException {
        Optional<User> userByUsername = userDao.getUserByUsername(userDto.getUsername());
        User user = userByUsername.orElseThrow((Supplier<UserNotFoundException>) () -> {
            throw new UserNotFoundException(userDto.getUsername());
        });

        if (!BCrypt.checkpw(userDto.getPassword(), user.getPassword())) {
            throw new InvalidUserPasswordException(userDto.getUsername());
        }
        return user.toDto();
    }
}
