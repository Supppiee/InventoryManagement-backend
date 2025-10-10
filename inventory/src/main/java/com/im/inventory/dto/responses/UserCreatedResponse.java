package com.im.inventory.dto.responses;

import lombok.Data;

@Data
public class UserCreatedResponse{
    private Long id;
    private String employeeCode;
    private String username;
    private String role;
    private String email;
}
