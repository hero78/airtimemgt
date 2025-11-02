package com.example.airtimemanagement.dto;

import com.example.airtimemanagement.domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private Set<Role> roles;
}
