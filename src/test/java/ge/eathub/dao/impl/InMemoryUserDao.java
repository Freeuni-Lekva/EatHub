package ge.eathub.dao.impl;

import ge.eathub.dao.UserDao;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.models.User;

import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserDao implements UserDao {
    List<User> users;
    AtomicLong count = new AtomicLong(1);

    public InMemoryUserDao() {
        users = new Vector<>();
    }


    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(Long userID) {
        return users.stream().filter(user ->
                user.getUserID()
                        .equals(userID))
                .findAny();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return users.stream().filter(user ->
                user.getUsername()
                        .equals(username))
                .findAny();
    }

    @Override
    public User createUser(User user) {
        if (users.stream().noneMatch(u ->
                u.getUsername().equals(user.getUsername()))) {
            User newUser = new User(user).setUserID(count.getAndIncrement());
            users.add(newUser);
            return newUser;
        }
        throw new UserCreationException(user.getUsername());
    }
}
