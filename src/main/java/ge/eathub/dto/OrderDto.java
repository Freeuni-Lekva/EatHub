package ge.eathub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Time;

public class OrderDto {
    @JsonIgnore
    private Long orderID;
    private String username;
    private String mealName;
    @JsonIgnore
    private Long roomID;
    @JsonProperty("amount")
    private Integer quantity;
    private Time cookingTime;
    private BigDecimal totalPrice;

    public OrderDto() {
    }

    public OrderDto(Long orderID, String username, String mealName, Long roomID, Integer quantity, Time cookingTime, BigDecimal totalPrice) {
        this.orderID = orderID;
        this.username = username;
        this.mealName = mealName;
        this.roomID = roomID;
        this.quantity = quantity;
        this.cookingTime = cookingTime;
        this.totalPrice = totalPrice;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderID=" + orderID +
                ", username='" + username + '\'' +
                ", mealName='" + mealName + '\'' +
                ", roomID=" + roomID +
                ", quantity=" + quantity +
                ", cookingTime=" + cookingTime +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
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

    public Time getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Time cookingTime) {
        this.cookingTime = cookingTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
