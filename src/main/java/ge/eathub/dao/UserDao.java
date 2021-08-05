package ge.eathub.dao;

import ge.eathub.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getAllUsers();

    Optional<User> getUserById(Long userID);

    Optional<User> getUserByUsername(String username);

    User createUser(User user);

    boolean confirmUserRegistration(String username);

}
