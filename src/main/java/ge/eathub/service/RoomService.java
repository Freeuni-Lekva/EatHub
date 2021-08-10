package ge.eathub.service;

import ge.eathub.dto.UserDto;
import ge.eathub.models.Room;

import java.util.Optional;

public interface RoomService {
    Optional<Room> createRoom(UserDto user, Long restaurantID);

    boolean inviteUser(String username, Long roomID);

    boolean checkUser(Long username, Long roomID);
}
