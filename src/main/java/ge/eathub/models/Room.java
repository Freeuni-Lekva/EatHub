package ge.eathub.models;

public class Room {
    public static final String TABLE = "rooms";
    public static final String ROOM_ID = "room_id";
    public static final String RESTAURANT_ID = "restaurant_id";
    public static final String ACTIVE = "active";

    public static final String ATTR = "ROOM";
    private Long roomID;
    private Long restaurantID;
    private Boolean active;

    public Room(Long roomID, Long restaurantID) {
        this.restaurantID = restaurantID;
        this.roomID = roomID;
        active = true;
    }

    public Room(Long roomID, Long restaurantID, Boolean active) {
        this.restaurantID = restaurantID;
        this.roomID = roomID;
        this.active = active;
    }

    public Room(Long restaurantID, Boolean active){
        this.restaurantID = restaurantID;
        this.active = active;
    }

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    public Long getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Long restaurantID) {
        this.restaurantID = restaurantID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", restaurantID=" + restaurantID +
                ", active=" + active +
                '}';
    }
}
