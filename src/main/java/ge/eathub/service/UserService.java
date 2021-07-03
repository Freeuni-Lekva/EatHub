package ge.eathub.service;

import ge.eathub.dto.UserLoginDto;
import ge.eathub.dto.UserRegisterDto;

import javax.validation.constraints.NotNull;

public interface UserService {

    boolean registerUser(@NotNull UserRegisterDto userDto);
    boolean LoginUser(@NotNull UserLoginDto userDto);

}
