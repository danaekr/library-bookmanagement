package com.library.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40)
    private String password;
    
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    
    private String firstName;
    private String lastName;
}
