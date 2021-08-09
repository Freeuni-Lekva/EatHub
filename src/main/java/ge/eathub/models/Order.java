package ge.eathub.models;

public class Order {
    public static final String TABLE = "orders";
    public static final String ORDER_ID = "order_id";
    public static final String USER_ID = "user_id";
    public static final String MEAL_ID = "meal_id";
    public static final String ROOM_ID = "room_id";
    public static final String QUANTITY = "quantity";

    private Long orderID;
    private Long userID;
    private Long mealID;
    private Long roomID;
    private Integer quantity;

    public Order (Long userID, Long mealID, Long roomID, Integer quantity) {
        this.userID = userID;
        this.mealID = mealID;
        this.roomID = roomID;
        this.quantity = quantity;
    }

    public Order (Long userID, Long mealID, Long roomID) {
        this.userID = userID;
        this.mealID = mealID;
        this.roomID = roomID;
        quantity = 1;
    }

    public Long getOrderID() { return orderID;}

    public void setOrderID(Long orderID) {this.orderID = orderID;}

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getMealID() {
        return mealID;
    }

    public void setMealID(Long mealID) {
        this.mealID = mealID;
    }

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
