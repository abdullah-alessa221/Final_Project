package com.example.finalprojectjavabootcamp.Service;

import com.example.finalprojectjavabootcamp.Api.ApiException;
import com.example.finalprojectjavabootcamp.Model.User;
import com.example.finalprojectjavabootcamp.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }

        return user;
    }

    public void deleteUser(Integer id){
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }
        userRepository.delete(user);
    }



}
