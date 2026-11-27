package com.yashcodes.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name Cannot exceed 100 Characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "please provide a valid email")
        @Size(max = 150, message = "Email cannot exceed 150 Characters")
        String email,

        @Size(max = 20, message = "Phone number cannot exceed 20 Characters")
        String phone
) {
}
