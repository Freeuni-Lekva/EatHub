package ge.eathub.service.impl;

import ge.eathub.dao.UserDao;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.models.User;
import ge.eathub.service.UserService;
import ge.eathub.utils.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    private final static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    // TODO Send registration email
    @Override
    public boolean registerUser(UserRegisterDto userDto) {
        try {
            if (!EmailValidator.validate(userDto.getEmail())) {
                logger.info("false email");
                return false;
            }
            User newUser = new User(userDto.getUsername(),
                    BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()),
                    userDto.getEmail());
            userDao.createUser(newUser);

        } catch (UserCreationException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean LoginUser(UserLoginDto userDto) {

        return false;
    }
}
