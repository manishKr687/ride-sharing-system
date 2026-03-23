package com.user.service.dto;

import com.user.service.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private Double currentLatitude;
    private Double currentLongitude;
}
