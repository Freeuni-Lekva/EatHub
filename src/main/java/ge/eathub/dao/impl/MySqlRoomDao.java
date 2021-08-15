package ge.eathub.dao.impl;

import ge.eathub.dao.RoomDao;
import ge.eathub.database.DBConnection;
import ge.eathub.dto.RoomDto;
import ge.eathub.exceptions.UserAlreadyInRoomException;
import ge.eathub.models.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ge.eathub.dao.impl.MySqlConstants.MYSQL_DUPLICATE_ERROR_CODE;

public class MySqlRoomDao implements RoomDao {

    private final DataSource dataSource;

    public MySqlRoomDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Room> createRoom(Long restaurantID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s) VALUES (?,?);".formatted(
                            Room.TABLE,
                            Room.RESTAURANT_ID,
                            Room.ACTIVE), Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, restaurantID);
            stm.setBoolean(2, true);
            if (stm.executeUpdate() == 1) {
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                Long roomID = rs.getLong(1);
                return Optional.of(new Room(roomID, restaurantID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public List<Room> getAllRooms() {
        Connection conn = null;
        List<Room> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT * FROM %s ;".formatted(Room.TABLE));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new Room(
                                rs.getLong(1),
                                rs.getLong(2),
                                rs.getBoolean(3)
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
    public Optional<Room> getRoomById(Long roomID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT %s, %s, %s FROM %s where %s = ?;".formatted(
                            Room.ROOM_ID,
                            Room.RESTAURANT_ID,
                            Room.ACTIVE,
                            Room.TABLE,
                            Room.ROOM_ID));
            stm.setLong(1, roomID);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return Optional.of(new Room(
                        rs.getLong(1),
                        rs.getLong(2),
                        rs.getBoolean(3)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public boolean closeRoom(Long roomID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE rooms SET active = FALSE WHERE %s = ?"
                            .formatted(Room.ROOM_ID));
            stm.setLong(1, roomID);
            if (stm.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public List<Room> getRoomsByUserID(Long userID) {
        Connection conn = null;
        List<Room> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(("select r.room_id, r.restaurant_id ,r.active " +
                    "from %s u join %s r on r.room_id = u.room_id " +
                    "join %s rest on r.restaurant_id = rest.restaurant_id " +
                    "where u.user_id = ? ;")
                    .formatted(UserRoom.TABLE, Room.TABLE, Restaurant.TABLE));
            stm.setLong(1, userID);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new Room(
                                rs.getLong(1),
                                rs.getLong(2),
                                rs.getBoolean(3)
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
    public List<User> getUsersByRoomID(Long roomID) {
        Connection conn = null;
        List<User> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "select %s, %s, %s, %s, %s, %s, %s  from %s INNER JOIN %s ON %s = %s where %s = ?;".formatted(
                            User.TABLE + "." + User.COLUMN_ID,
                            User.TABLE + "." + User.COLUMN_USERNAME,
                            User.TABLE + "." + User.COLUMN_PASSWORD,
                            User.TABLE + "." + User.COLUMN_EMAIL,
                            User.TABLE + "." + User.COLUMN_BALANCE,
                            User.TABLE + "." + User.COLUMN_ROLE,
                            User.TABLE + "." + User.COLUMN_CONFIRMED,
                            UserRoom.TABLE,
                            User.TABLE,
                            User.TABLE + "." + User.COLUMN_ID,
                            UserRoom.TABLE + "." + UserRoom.USER_ID,
                            UserRoom.TABLE + "." + UserRoom.ROOM_ID));
            stm.setLong(1, roomID);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new User(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getBigDecimal(5),
                                Role.valueOf(rs.getString(6)),
                                rs.getBoolean(7)
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
    public void addUserIntoRoom(Long roomID, Long userID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s) VALUES (?,?);".formatted(
                            UserRoom.TABLE,
                            UserRoom.ROOM_ID,
                            UserRoom.USER_ID));
            stm.setLong(1, roomID);
            stm.setLong(2, userID);
            stm.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == MYSQL_DUPLICATE_ERROR_CODE) {
                throw new UserAlreadyInRoomException(userID.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Override
    public boolean userInRoom(Long roomID, Long userID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "select * from %s where %s = ? and %s = ?;".formatted(
                            UserRoom.TABLE,
                            UserRoom.ROOM_ID,
                            UserRoom.USER_ID));
            stm.setLong(1, roomID);
            stm.setLong(2, userID);
            return stm.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public boolean mealInRoom(Long mealID, Long roomID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "select * from %s INNER JOIN %s ON %s = %s WHERE %s = ? AND %s = ?;".formatted(
                            Meal.TABLE,
                            Room.TABLE,
                            Meal.TABLE + "." + Meal.COLUMN_RESTAURANT_ID,
                            Room.TABLE + "." + Room.RESTAURANT_ID,
                            Meal.COLUMN_ID,
                            Room.ROOM_ID));
            stm.setLong(1, mealID);
            stm.setLong(2, roomID);
            return stm.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public Optional<Restaurant> getRestaurantByRoomID(Long roomID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "select res.* from rooms r " +
                            "INNER JOIN restaurants res " +
                            "on r.restaurant_id = res.restaurant_id " +
                            "WHERE r.room_id = ?;");
            stm.setLong(1, roomID);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return Optional.of(new Restaurant(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getLong(4),
                        rs.getBigDecimal(5),
                        rs.getBigDecimal(6),
                        rs.getString(7)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public boolean isRoomActive(Long roomID) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "select active from rooms " +
                            " where room_id = ?");
            stm.setLong(1, roomID);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public List<RoomDto> getAllRoomByUserID(long userID) {
        Connection conn = null;
        List<RoomDto> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement("select r.room_id, r.active, rest.restaurant_name, rest.location from %s u join %s r on r.room_id = u.room_id join %s rest on r.restaurant_id = rest.restaurant_id where u.user_id = ? ;"
                    .formatted(UserRoom.TABLE, Room.TABLE, Restaurant.TABLE));
            stm.setLong(1, userID);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new RoomDto(
                                rs.getLong(1),
                                rs.getBoolean(2),
                                rs.getString(3),
                                rs.getString(4)
                        )
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return ret;
    }

    @Override
    public List<RoomDto> getAllRoomDto() {
        Connection conn = null;
        List<RoomDto> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement("select r.room_id, r.active,rest.restaurant_name, rest.location from %s r join %s rest on r.restaurant_id = rest.restaurant_id;"
                    .formatted(Room.TABLE, Restaurant.TABLE));
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new RoomDto(
                                rs.getLong(1),
                                rs.getBoolean(2),
                                rs.getString(3),
                                rs.getString(4)
                        )
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return ret;
    }
}