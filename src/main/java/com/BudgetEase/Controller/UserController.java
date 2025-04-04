package com.BudgetEase.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.BudgetEase.BudgetEaseService.UserService;
import com.BudgetEase.Exceptions.InvalidUserCredentialsException;
import com.BudgetEase.Models.User;
import com.BudgetEase.dtos.LoginUserRequest;
import com.BudgetEase.dtos.NewPasswordValidate;
import com.BudgetEase.dtos.RegisterUserRequest;
import com.BudgetEase.dtos.UpdateProfile;
import com.BudgetEase.utils.ApiResponse;
import com.BudgetEase.utils.FileUpload;
import com.BudgetEase.utils.GetCurrentUser;
import com.cloudinary.Api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController @RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request){
        try {
            userService.registerUser(request.getEmail(), request.getUsername(), request.getPassword(), request.getPhoneNo());

            return ResponseEntity.ok(new ApiResponse("User registered!"));
            
        } catch (InvalidUserCredentialsException e) {
            return ResponseEntity.status(400).body(new ApiResponse(e.getMessage()));
        }
    
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserRequest request, HttpServletResponse response){
        try {
            String token = userService.loginUser(request.getIdentifier(), request.getPassword());

            return ResponseEntity.ok()
                .header("Authorization", "Bearer "+token)
                .body(new ApiResponse("User logged in"));
        } catch (InvalidUserCredentialsException e) {
            return ResponseEntity.status(401).body(new ApiResponse(e.getMessage()));
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getAllUsers(){
        List<User> checkUsers = userService.getAllUsers();

        if(checkUsers.size()!=0){
            return ResponseEntity.ok().body(checkUsers);
        }

        return ResponseEntity.ok(new ApiResponse("No users found"));
    }

    @PostMapping(value = "/profile", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadProfile(@Valid @ModelAttribute UpdateProfile request) {
        // Get the current user
        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);

        if (getCurrentUser.obtainUser() == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User with id not found!"));
        }

        User user = getCurrentUser.obtainUser();

        // Validate and process the uploaded file
        MultipartFile userProfilePic = request.getUserProfilePic();
        if (userProfilePic == null || userProfilePic.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("Profile picture is required!"));
        }

        FileUpload fileUploader = new FileUpload();
        if (!fileUploader.isImageFile(userProfilePic)) {
            return ResponseEntity.status(400).body(new ApiResponse("Not an image file!"));
        }

        String cloudinaryUrl = "";
        try {
            cloudinaryUrl = fileUploader.uploadFile(userProfilePic);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ApiResponse("File upload failed: " + e.getMessage()));
        }

        // Update the user's profile
        userService.updateProfile(user.getUserId(), request.getEmail(), request.getUserName(), cloudinaryUrl);

        return ResponseEntity.ok(new ApiResponse("User profile updated successfully!"));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody NewPasswordValidate request) {
        // Get the current user
        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);
        User user = getCurrentUser.obtainUser();

        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found!"));
        }

        // Change the user's password
        userService.changePassword(user.getUserId(), request.getNewPassword());

        return ResponseEntity.ok(new ApiResponse("Password updated successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(){
        GetCurrentUser getCurrentUser = new GetCurrentUser(userService);
        User user = getCurrentUser.obtainUser();

        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found!"));
        }

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(new ApiResponse("User logged out"));
    }
}
