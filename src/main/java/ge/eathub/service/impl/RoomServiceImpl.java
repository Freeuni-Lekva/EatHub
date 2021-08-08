package ge.eathub.service.impl;

import ge.eathub.dao.RoomDao;
import ge.eathub.dao.UserDao;
import ge.eathub.dto.UserDto;
import ge.eathub.models.Room;
import ge.eathub.models.User;
import ge.eathub.service.RoomService;

import java.util.Optional;

public class RoomServiceImpl implements RoomService {
    private final RoomDao roomDao;
    private final UserDao userDao;

    public RoomServiceImpl(RoomDao roomDao, UserDao userDao) {
        this.roomDao = roomDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<Room> createRoom(UserDto user, Long restaurantID) {
        Optional<Room> newRoom = roomDao.createRoom(restaurantID);
        newRoom.ifPresent(room -> roomDao.addUserIntoRoom(room.getRoomID(), user.getUserID()));
        return newRoom;
    }

    @Override
    public boolean inviteUser(String username, Long roomID) {
        Optional<User> user = userDao.getUserByUsername(username);
        if (user.isEmpty()) {
            return  false;
        } else {
            roomDao.addUserIntoRoom(roomID, user.get().getUserID());
            // send mail
            return true;
        }
    }

    @Override
    public boolean checkUser(Long userID, Long roomID) {
        return roomDao.userInRoom(userID, roomID);
    }
}
