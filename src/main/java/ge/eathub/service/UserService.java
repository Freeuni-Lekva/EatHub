package ge.eathub.service;

import ge.eathub.dto.UserDto;
import ge.eathub.dto.UserLoginDto;
import ge.eathub.dto.UserRegisterDto;
import ge.eathub.exceptions.InvalidEmailException;
import ge.eathub.exceptions.InvalidUserPasswordException;
import ge.eathub.exceptions.UserCreationException;
import ge.eathub.exceptions.UserNotFoundException;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface UserService {

    UserDto registerUser(@NotNull UserRegisterDto userDto) throws UserCreationException, InvalidEmailException;

    UserDto loginUser(@NotNull UserLoginDto userDto) throws UserNotFoundException, InvalidUserPasswordException;

    boolean confirmUserRegistration(@NotNull String token);

    Optional<UserDto> getUserDtoByUsername(String username);
}
