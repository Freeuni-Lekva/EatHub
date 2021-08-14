package ge.eathub.service.impl;

import ge.eathub.dao.RoomDao;
import ge.eathub.dao.UserDao;
import ge.eathub.dto.RoomDto;
import ge.eathub.dto.UserDto;
import ge.eathub.exceptions.UserNotFoundException;
import ge.eathub.mailer.Mailer;
import ge.eathub.mailer.mails.InvitationMail;
import ge.eathub.models.Room;
import ge.eathub.models.User;
import ge.eathub.service.RoomService;

import java.util.List;
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
    public void inviteUser(String fromUsername, String invitedUsername, Long roomID) {
        Optional<User> userOptional = userDao.getUserByUsername(invitedUsername);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(invitedUsername);
        } else {
            User user = userOptional.get();
            roomDao.addUserIntoRoom(roomID, user.getUserID());
            new Thread(() -> Mailer.sendMail(
                    new InvitationMail(fromUsername, invitedUsername, user.getEmail(), roomID)))
                    .start();
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
    public List<RoomDto> getAllRoomByUserID(long userID) {
        return roomDao.getAllRoomByUserID(userID);
    }

    @Override
    public List<RoomDto> getAllRoomDto() {
        return roomDao.getAllRoomDto();
    }
}
