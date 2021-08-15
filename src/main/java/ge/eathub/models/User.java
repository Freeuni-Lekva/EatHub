package ge.eathub.models;

import ge.eathub.dto.UserDto;

import java.math.BigDecimal;
import java.util.Objects;

public class User {
    public static final String TABLE = "users";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_CONFIRMED = "confirmed";

    private Long userID; // autoincrement
    private String username;
    private String password;
    private String email;
    private BigDecimal balance = BigDecimal.TEN;
    private Role role = Role.CUSTOMER;
    private Boolean confirmed;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.confirmed = false;
    }

    public User(Long userID, String username, String password, String email, BigDecimal balance, Role role, Boolean confirmed) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.balance = balance;
        this.role = role;
        this.confirmed = confirmed;
    }

    public User(User user) {
        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.balance = user.getBalance();
        this.role = user.getRole();
        this.confirmed = user.getConfirmed();
    }

    public User() {
    }

    public static User builder() {
        return new User();
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }


    public Long getUserID() {
        return userID;
    }

    public User setUserID(Long userID) {
        this.userID = userID;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public User setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public UserDto toDto() {
//        return UserDto.builder()
//                .setUsername(username)
//                .setEmail(email)
//                .setBalance(balance)
//                .setRole(role);
        return new UserDto(userID, username, email, balance, role, confirmed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                ", role=" + role +
                ", confirmed=" + confirmed +
                '}';
    }
}