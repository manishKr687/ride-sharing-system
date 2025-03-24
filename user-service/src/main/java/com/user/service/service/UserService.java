package com.user.service.service;

import com.user.service.entity.Users;
import com.user.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Users registerUser(Users users){
        return userRepository.save(users);
    }

    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long userId){
        return userRepository.findById(userId);
    }

    public Optional<Users> getByEmail(String email){

        return userRepository.findByEmail(email);
    }

    public void updateLocation(Long userId, Double longitude, Double latitude){
        Users users = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found"));
        users.setCurrentLongitude(longitude);
        users.setCurrentLatitude(latitude);
        userRepository.save(users);
    }
}
