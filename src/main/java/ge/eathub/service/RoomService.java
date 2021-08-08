package ge.eathub.service;

import ge.eathub.dto.UserDto;
import ge.eathub.models.Room;

public interface RoomService {
    Room createRoom(UserDto user, Long restaurantID);

    boolean inviteUser(String username, Long roomID);

    boolean joinRoom(String username, Long roomID);
}
