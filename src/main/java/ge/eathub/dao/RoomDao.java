package ge.eathub.dao;

import ge.eathub.models.Room;
import ge.eathub.models.User;

import java.util.List;
import java.util.Optional;

public interface RoomDao {
    Optional<Room> createRoom(Long restaurantID);

    List<Room> getAllRooms();

    Optional<Room> getRoomById(Long roomID);

    void closeRoom(Room room);

    // Optional<Room> getUserByUsername(String username);

    List<Room> getRoomsByUserID(Long userID);

    List<User> getUsersByRoomID(Long roomID);

    void addUserIntoRoom(Long roomID, Long userID);

    boolean userInRoom(Long roomID, Long userID);

    boolean mealInRoom(Long mealID, Long roomID);

    //boolean removeUserFromRoom(Long roomID, Long userID);
}
