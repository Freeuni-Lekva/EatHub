package ge.eathub.service.impl;

import ge.eathub.dao.UserDao;
import ge.eathub.dto.UserDto;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.InvalidEmailException;
import ge.eathub.exceptions.InvalidUserPasswordException;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.exceptions.UserNotFoundException;
import ge.eathub.mailer.Mailer;
import ge.eathub.mailer.mails.RegistrationMail;
import ge.eathub.models.User;
import ge.eathub.security.Authenticator;
import ge.eathub.service.UserService;
import ge.eathub.utils.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static ge.eathub.Main.DEBUG;

public class UserServiceImpl implements UserService {
    private final static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final static Authenticator authenticator = new Authenticator(15);
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    // TODO Send registration email
    @Override
    public UserDto registerUser(UserRegisterDto userDto) throws UserCreationException, InvalidEmailException {
        logger.info("create user " + userDto.getUsername());

        if (!DEBUG && !EmailValidator.validate(userDto.getEmail())) {
            throw new InvalidEmailException(userDto.getEmail());
        }
        if (userDao.checkInfo(userDto.getUsername(), userDto.getEmail())) {
            if (DEBUG || Mailer.sendMail(new RegistrationMail(userDto.getUsername(), userDto.getEmail(),
                    authenticator.getAccessToken(userDto.getUsername())))) {
                logger.info("email was sent");
                User newUser = new User(userDto.getUsername(),
                        BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()),
                        userDto.getEmail());
                User createdUser = userDao.createUser(newUser);
                return createdUser.toDto();
            }
            throw new InvalidEmailException(userDto.getEmail());
        }
        throw new UserCreationException("unknown error | user " + userDto.getUsername());
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

    @Override
    public boolean confirmUserRegistration(String token) {
        Optional<String> usernameOpt = authenticator.getUsername(token);
        if (usernameOpt.isPresent()) {
            logger.info("user registration confirmed");
            return userDao.confirmUserRegistration(usernameOpt.get());

        }
        return false;
    }
}
