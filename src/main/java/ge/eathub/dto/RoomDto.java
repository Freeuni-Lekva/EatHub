package ge.eathub.dto;

public class RoomDto {
    private long roomID;
    private boolean active;
    private String restaurantName;
    private String restaurantLocation;

    public RoomDto(long roomID, boolean active, String restaurantName, String restaurantLocation) {
        this.roomID = roomID;
        this.active = active;
        this.restaurantName = restaurantName;
        this.restaurantLocation = restaurantLocation;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(String restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }
}
