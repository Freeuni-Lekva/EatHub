package ge.eathub.service.impl;

import ge.eathub.dao.RoomDao;
import ge.eathub.dao.UserDao;
import ge.eathub.dto.UserDto;
import ge.eathub.mailer.Mailer;
import ge.eathub.mailer.mails.InvitationMail;
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
    public boolean inviteUser(String fromUsername, String invitedUsername, Long roomID) {
        Optional<User> userOptional = userDao.getUserByUsername(invitedUsername);
        if (userOptional.isEmpty()) {
            return false;
        } else {
            User user = userOptional.get();
            roomDao.addUserIntoRoom(roomID, user.getUserID());
            new Thread(() -> Mailer.sendMail(
                    new InvitationMail(fromUsername, invitedUsername, user.getEmail(), roomID)))
                    .start();
            return true;
        }
    }

    @Override
    public boolean checkUser(Long roomID, Long userID) {
        return roomDao.userInRoom(roomID, userID);
    }

    // better to use only check user and that method should return Optional<Room>
    @Override
    public Optional<Room> getRoomByID(Long roomID) {
        return roomDao.getRoomById(roomID);
    }

    @Override
    public boolean closeRoom(Long roomID) {
        return roomDao.closeRoom(roomID);
    }

    @Override
    public boolean isRoomActive(Long roomID) {
        return roomDao.isRoomActive(roomID);
    }
}
