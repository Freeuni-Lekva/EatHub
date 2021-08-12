package ge.eathub.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    public static final String TABLE = "orders";
    public static final String ORDER_ID = "order_id";
    public static final String USER_ID = "user_id";
    public static final String MEAL_ID = "meal_id";
    public static final String ROOM_ID = "room_id";
    public static final String QUANTITY = "quantity";
    @JsonIgnore
    private Long orderID;
    @JsonIgnore
    private Long userID;
    @JsonProperty("mealId")
    private Long mealID;
    @JsonIgnore
    private Long roomID;
    @JsonProperty("amount")
    private Integer quantity;

    public Order() {
    }

    public Order(Long orderID, Long userID, Long mealID, Long roomID, Integer quantity) {
        this.orderID = orderID;
        this.userID = userID;
        this.mealID = mealID;
        this.roomID = roomID;
        this.quantity = quantity;
    }

    public Order(Long userID, Long mealID, Long roomID, Integer quantity) {
        this.userID = userID;
        this.mealID = mealID;
        this.roomID = roomID;
        this.quantity = quantity;
    }

    public Order(Long userID, Long mealID, Long roomID) {
        this.userID = userID;
        this.mealID = mealID;
        this.roomID = roomID;
        quantity = 1;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

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

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", userID=" + userID +
                ", mealID=" + mealID +
                ", roomID=" + roomID +
                ", quantity=" + quantity +
                '}';
    }
}
