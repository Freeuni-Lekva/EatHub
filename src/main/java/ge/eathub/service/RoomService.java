package ge.eathub.service;

import ge.eathub.dto.RoomDto;
import ge.eathub.dto.UserDto;
import ge.eathub.models.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    Optional<Room> createRoom(UserDto user, Long restaurantID);

    void inviteUser(String fromUsername, String invitedUsername, Long roomID);

    boolean checkUser(Long roomID, Long userID);

    Optional<Room> getRoomByID(Long roomID);

    boolean closeRoom(Long roomID);

    boolean isRoomActive(Long roomID);


    List<RoomDto> getAllRoomByUserID(long userID);

    List<RoomDto> getAllRoomDto();

}
