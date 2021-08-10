package ge.eathub.dao.impl;

import ge.eathub.dao.RoomDao;
import ge.eathub.database.DBConnection;
import ge.eathub.models.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                return Optional.of(new Room(restaurantID, roomID));
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
            rs.next();
            return Optional.of(new Room(
                    rs.getLong(1),
                    rs.getLong(2),
                    rs.getBoolean(3)
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public void closeRoom(Room room) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "UPDATE rooms SET active = FALSE WHERE room_id = ?");
            stm.setLong(1, room.getRoomID());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Override
    public List<Room> getRoomsByUserID(Long userID) {
        Connection conn = null;
        List<Room> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "select %s, %s, %s  from %s INNER JOIN %s ON ? = ? where ? = ?;".formatted(
                            Room.TABLE + "." + Room.ROOM_ID,
                            Room.TABLE + "." + Room.RESTAURANT_ID,
                            Room.TABLE + "." + Room.ACTIVE,
                            UserRoom.TABLE,
                            Room.TABLE));
            stm.setString(1, Room.TABLE + "." + Room.ROOM_ID);
            stm.setString(2, UserRoom.TABLE + "." + UserRoom.ROOM_ID);
            stm.setString(3, UserRoom.USER_ID);
            stm.setLong(4, userID);
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
                    "select %s, %s, %s, %s, %s, %s, %s  from %s INNER JOIN %s ON ? = ? where ? = ?;".formatted(
                            User.TABLE + "." + User.COLUMN_ID,
                            User.TABLE + "." + User.COLUMN_USERNAME,
                            User.TABLE + "." + User.COLUMN_PASSWORD,
                            User.TABLE + "." + User.COLUMN_EMAIL,
                            User.TABLE + "." + User.COLUMN_BALANCE,
                            User.TABLE + "." + User.COLUMN_ROLE,
                            User.TABLE + "." + User.COLUMN_CONFIRMED,
                            UserRoom.TABLE,
                            User.TABLE));
            stm.setString(1, User.TABLE + "." + User.COLUMN_ID);
            stm.setString(2, UserRoom.TABLE + "." + UserRoom.USER_ID);
            stm.setString(3, UserRoom.ROOM_ID);
            stm.setLong(4, roomID);
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
}
