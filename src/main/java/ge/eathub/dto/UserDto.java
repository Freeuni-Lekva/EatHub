package ge.eathub.dto;

import ge.eathub.models.Role;

import java.math.BigDecimal;

public class UserDto {
    public static final String ATTR = "userDto";
    private String username;
    private String email;
    private BigDecimal balance;
    private Role role;

    public UserDto(String username, String email, BigDecimal balance, Role role) {
        this.username = username;
        this.email = email;
        this.balance = balance;
        this.role = role;
    }

    public UserDto() {
    }

    public String getUsername() {
        return username;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public UserDto setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public UserDto setRole(Role role) {
        this.role = role;
        return this;
    }

    public static UserDto builder() {
        return new UserDto();
    }
}
