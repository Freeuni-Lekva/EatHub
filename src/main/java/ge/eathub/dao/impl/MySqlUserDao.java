package ge.eathub.dao.impl;

import ge.eathub.dao.UserDao;
import ge.eathub.database.DBConnection;
import ge.eathub.exceptions.EmailAlreadyExistsException;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.exceptions.UsernameAlreadyExistsException;
import ge.eathub.models.Role;
import ge.eathub.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlUserDao implements UserDao {

    private final DataSource dataSource;
    public static final int MYSQL_DUPLICATE_ERROR_CODE = 1062;

    public MySqlUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> getAllUsers() {
        Connection conn = null;
        List<User> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT %s, %s, %s, %s, %s, %s FROM %s ;".formatted(User.COLUMN_ID,
                            User.COLUMN_USERNAME, User.COLUMN_PASSWORD, User.COLUMN_EMAIL,
                            User.COLUMN_BALANCE, User.COLUMN_ROLE, User.TABLE));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new User(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getBigDecimal(5),
                                Role.valueOf(rs.getString(6))
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return ret;
    }

    @Override
    public Optional<User> getUserById(Long userID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT %s, %s, %s, %s, %s, %s FROM %s where %s = ?;".formatted(User.COLUMN_ID,
                            User.COLUMN_USERNAME, User.COLUMN_PASSWORD, User.COLUMN_EMAIL,
                            User.COLUMN_BALANCE, User.COLUMN_ROLE, User.TABLE, User.COLUMN_ID));
            stm.setLong(1, userID);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return Optional.of(new User(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getBigDecimal(5),
                    Role.valueOf(rs.getString(6))
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT %s, %s, %s, %s, %s, %s FROM %s where %s = ?;".formatted(User.COLUMN_ID,
                            User.COLUMN_USERNAME, User.COLUMN_PASSWORD, User.COLUMN_EMAIL,
                            User.COLUMN_BALANCE, User.COLUMN_ROLE, User.TABLE, User.COLUMN_USERNAME));
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            rs.next();
            return Optional.of(new User(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getBigDecimal(5),
                    Role.valueOf(rs.getString(6))
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();

    }

    @Override
    public User createUser(User user) throws UserCreationException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?);  ;".formatted(
                            User.TABLE,
                            User.COLUMN_USERNAME, User.COLUMN_PASSWORD, User.COLUMN_EMAIL,
                            User.COLUMN_BALANCE, User.COLUMN_ROLE), Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getEmail());
            stm.setBigDecimal(4, user.getBalance());
            stm.setString(5, user.getRole().name());
            if (stm.executeUpdate() == 1) {
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                Long newUserID = rs.getLong(1);
                return new User(user).setUserID(newUserID);
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_ERROR_CODE) {
                if (e.getMessage().contains("%s.%s".formatted(User.TABLE, User.COLUMN_USERNAME))) {
                    throw new UsernameAlreadyExistsException(user.getUsername());
                }
                if (e.getMessage().contains("%s.%s".formatted(User.TABLE, User.COLUMN_EMAIL))) {
                    throw new EmailAlreadyExistsException(user.getEmail());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        throw new UserCreationException("unknown error | user" + user.getUsername());
    }

}
