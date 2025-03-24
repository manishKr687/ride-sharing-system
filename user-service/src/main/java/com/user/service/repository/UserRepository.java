package com.user.service.repository;

import com.user.service.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
//    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<Users> findByEmail(String email);
}
