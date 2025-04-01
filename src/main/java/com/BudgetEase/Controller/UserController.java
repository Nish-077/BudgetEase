package com.BudgetEase.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BudgetEase.BudgetEaseService.UserService;
import com.BudgetEase.Exceptions.InvalidUserCredentialsException;
import com.BudgetEase.Models.User;
import com.BudgetEase.dtos.LoginUserRequest;
import com.BudgetEase.dtos.RegisterUserRequest;
import com.BudgetEase.utils.ApiResponse;
import com.cloudinary.Api;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController @RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

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


}
