package com.pm.saaspossystem.services;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.model.User;
import com.pm.saaspossystem.payload.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserFromJwt(String token) throws UserExceptions;
    UserDto getCurrentUser() throws UserExceptions;
    UserDto getUserByEmail(String email) throws UserExceptions;
    UserDto getUserById(Long id) throws UserExceptions;
    List<UserDto> getAllUsers();
}
