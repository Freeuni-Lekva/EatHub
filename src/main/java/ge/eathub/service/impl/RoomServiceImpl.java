package ge.eathub.service.impl;

import ge.eathub.dto.UserDto;
import ge.eathub.models.Room;
import ge.eathub.service.RoomService;

public class RoomServiceImpl implements RoomService {


    @Override
    public Room createRoom(UserDto user, Long restaurantID) {
        return null;
    }

    @Override
    public boolean inviteUser(String username, Long roomID) {
        return false;
    }

    @Override
    public boolean joinRoom(String username, Long roomID) {
        return false;
    }
}
