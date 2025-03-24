package com.user.service.controller;

import com.user.service.entity.Users;
import com.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users users){
        return new ResponseEntity<Users>(userService.registerUser(users), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Users>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<Users>> getAllUsers(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Optional<Users>> getAllUsers(@RequestParam String email){
        Optional<Users> user = userService.getByEmail(email);
        if (user.isPresent()) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //return new ResponseEntity<>(userService.getByEmail(email), HttpStatus.OK);
    }

    @PutMapping("/update-location/{userId}")
    public ResponseEntity<String> updateLocation(@PathVariable Long userId, @RequestParam Double latitude, @RequestParam Double longitude){
        userService.updateLocation(userId, latitude, longitude);
        return ResponseEntity.ok("Location Updated");
    }

}
