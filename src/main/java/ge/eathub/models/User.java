package ge.eathub.models;

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

    private Long userID; // autoincrement
    private String username;
    private String password;
    private String email;
    private BigDecimal balance = BigDecimal.TEN;
    private Role role = Role.CUSTOMER;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(Long userID, String username, String password, String email, BigDecimal balance, Role role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.balance = balance;
        this.role = role;
    }

    public User(User user) {
        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.balance = user.getBalance();
        this.role = user.getRole();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}