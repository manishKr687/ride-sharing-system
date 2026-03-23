package com.user.service.controller;

import com.user.service.dto.UserRequestDTO;
import com.user.service.dto.UserResponseDTO;
import com.user.service.entity.Users;
import com.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO users){
        return new ResponseEntity<>(userService.registerUser(users), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getAllUsers(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseDTO> getAllUsers(@RequestParam String email){
        return new ResponseEntity<>(userService.getByEmail(email), HttpStatus.OK);
    }

    @PutMapping("/update-location/{userId}")
    public ResponseEntity<String> updateLocation(@PathVariable Long userId, @RequestParam Double latitude, @RequestParam Double longitude){
        userService.updateLocation(userId, latitude, longitude);
        return ResponseEntity.ok("Location Updated");
    }

}
