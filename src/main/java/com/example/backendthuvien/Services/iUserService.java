package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.UpdateUserDTO;
import com.example.backendthuvien.DTO.UserDTO;
import com.example.backendthuvien.entity.User;
import com.example.backendthuvien.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;


public interface iUserService {
    User creatUser(UserDTO userDTO) throws  Exception;

    String login(String phoneNumber,String password,long roleId) throws Exception;

    User getUserDetailFromToken(String token) throws Exception;
    User updateUser(long userId, UpdateUserDTO userDTO) throws Exception;
}
