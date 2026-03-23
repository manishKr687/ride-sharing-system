package com.user.service.service;

import com.user.service.config.JwtUtil;
import com.user.service.dto.AuthResponseDTO;
import com.user.service.dto.LoginRequestDTO;
import com.user.service.dto.UserRequestDTO;
import com.user.service.dto.UserResponseDTO;
import com.user.service.entity.Users;
import com.user.service.exception.InvalidCredentialsException;
import com.user.service.exception.UserNotFoundException;
import com.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO registerUser(UserRequestDTO request) {
        Users user = new Users();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setCurrentLatitude(request.getCurrentLatitude());
        user.setCurrentLongitude(request.getCurrentLongitude());

        Users saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getUserId(), saved.getEmail(), saved.getRole().name());
        return toAuthResponse(saved, token);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole().name());
        return toAuthResponse(user, token);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponseDTO getUserById(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return mapToResponse(user);
    }

    public UserResponseDTO getByEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return mapToResponse(user);
    }

    @Transactional
    public void updateLocation(Long userId, Double latitude, Double longitude) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.setCurrentLatitude(latitude);
        user.setCurrentLongitude(longitude);
        userRepository.save(user);
    }

    private AuthResponseDTO toAuthResponse(Users user, String token) {
        return AuthResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .token(token)
                .build();
    }

    private UserResponseDTO mapToResponse(Users user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .currentLatitude(user.getCurrentLatitude())
                .currentLongitude(user.getCurrentLongitude())
                .build();
    }
}
