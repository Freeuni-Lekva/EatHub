package ge.eathub.models;

public class UserRoom {
    public static final String TABLE = "user_room";
    public static final String ROOM_ID = "room_id";
    public static final String USER_ID = "user_id";

    private Long roomID;
    private Long userID;

    public UserRoom(Long roomID, Long userID) {
        this.roomID = roomID;
        this.userID = userID;
    }

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
