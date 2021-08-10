package ge.eathub.dao.impl;

import ge.eathub.dao.ChatDao;
import ge.eathub.database.DBConnection;
import ge.eathub.models.chat.Message;
import ge.eathub.models.chat.MessageType;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MySqlChatDao implements ChatDao {
    private final DataSource dataSource;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MySqlChatDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean saveMessage(Message msg) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?);  ;".formatted(
                            Message.TABLE,
                            Message.USER_ID,
                            Message.ROOM_ID,
                            Message.SEND_TIME,
                            Message.TYPE,
                            Message.CONTENT
                    ), Statement.RETURN_GENERATED_KEYS);
            stm.setLong(1, msg.getUserID());
            stm.setLong(2, msg.getRoomID());
            stm.setString(3, msg.getSendTime().format(formatter));
            stm.setString(4, msg.getType().name());
            stm.setString(5, msg.getContent());
            if (stm.executeUpdate() == 1) {
                ResultSet rs = stm.getGeneratedKeys();
                rs.next();
                Long messageID = rs.getLong(1);
                msg.setMessageID(messageID);
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
    public List<Message> getMessagesByRoomID(Long roomID) {
        Connection conn = null;
        List<Message> ret = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stm = conn.prepareStatement(
                    "SELECT m.*, u.username FROM messages m " +
                            "LEFT JOIN users u on u.user_id = m.user_id " +
                            "WHERE m.room_id=? " +
                            "ORDER BY m.message_id;"
            );
            stm.setLong(1, roomID);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(new Message(
                                rs.getLong(1),
                                rs.getString(7),
                                rs.getLong(2),
                                rs.getLong(3),
                                LocalDateTime.parse(rs.getString(4), formatter),
                                MessageType.valueOf(rs.getString(5)),
                                rs.getString(6)
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
    public List<Message> getMessagesByRoomID(Long roomID, Integer num) {
        return null;
    }
}
