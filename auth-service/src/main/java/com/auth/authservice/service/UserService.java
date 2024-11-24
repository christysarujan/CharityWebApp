package com.auth.authservice.service;

import com.auth.authservice.entity.User;
import com.auth.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(" user not found with id: "+ id));

    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

}
