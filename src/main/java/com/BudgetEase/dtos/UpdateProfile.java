package com.BudgetEase.dtos;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfile {

    @NotBlank
    private String userName;

    @NotNull
    private MultipartFile userProfilePic;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNo;
}
