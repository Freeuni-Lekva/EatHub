package ge.eathub.service;

import ge.eathub.dto.UserLoginDto;
import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.InvalidEmailException;
import ge.eathub.exceptions.UserCreationException;

import javax.validation.constraints.NotNull;

public interface UserService {

    void registerUser(@NotNull UserRegisterDto userDto) throws UserCreationException, InvalidEmailException;
    boolean LoginUser(@NotNull UserLoginDto userDto) throws Throwable;

}
