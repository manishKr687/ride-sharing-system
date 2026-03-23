package com.user.service.service;

import com.user.service.dto.UserRequestDTO;
import com.user.service.dto.UserResponseDTO;
import com.user.service.entity.Users;
import com.user.service.exception.UserNotFoundException;
import com.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO registerUser(UserRequestDTO request){
        Users users = new Users();
        users.setName(request.getName());
        users.setEmail(request.getEmail());
        users.setRole(request.getRole());
        users.setCurrentLatitude(request.getCurrentLatitude());
        users.setCurrentLongitude(request.getCurrentLongitude());
        return mapToResponse(userRepository.save(users));
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    public UserResponseDTO getUserById(Long userId){
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        return mapToResponse(user);
    }

    public UserResponseDTO getByEmail(String email){
        Users user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
        return mapToResponse(user);
    }

    public void updateLocation(Long userId, Double longitude, Double latitude){
        Users users = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        users.setCurrentLongitude(longitude);
        users.setCurrentLatitude(latitude);
        userRepository.save(users);
    }

    private UserResponseDTO mapToResponse(Users user) {
        return UserResponseDTO.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole())
            .currentLatitude(user.getCurrentLatitude())
            .currentLongitude(user.getCurrentLongitude())
            .build();
    }
}
