package ge.eathub.dao;

import ge.eathub.models.Room;
import ge.eathub.models.User;
import ge.eathub.models.chat.Message;

import java.util.List;
import java.util.Optional;

public interface RoomDao {
    Optional<Room> createRoom(Long restaurantID);

    List<Room> getAllRooms();

    Optional<Room> getRoomById(Long roomID);

    void closeRoom(Room room);

    // Optional<Room> getUserByUsername(String username);

    Optional<Room> getUserByID (Long userID);

    List<User> getUsersByRoomID(Long roomID);

    void addUserIntoRoom(Long roomID, Long userID);

    boolean removeUserFromRoom(Long roomID, Long userID);

    List<Message> getMessagesByRoomID(Long roomID);
}
