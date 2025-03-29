package com.BudgetEase.BudgetEaseService;


import com.BudgetEase.Exceptions.InvalidUserCredentialsException;
import com.BudgetEase.Exceptions.UserAlreadyExistsException;
import com.BudgetEase.Models.User;
import com.BudgetEase.repository.UserRepository;
import com.BudgetEase.utils.PasswordUtil;
import com.BudgetEase.utils.ValidateEmail;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    @SuppressWarnings("unchecked")
    public void registerUser(User user) {
        Optional<User> registeredUser = repository.findByEmail(user.getEmail());
        if(registeredUser.isPresent()){
            throw new UserAlreadyExistsException("Email already exists");
        }

        registeredUser = repository.findByUserName(user.getUserName());
        if(registeredUser.isPresent()){
            throw new UserAlreadyExistsException("Username already taken");
        }

        user.setPasswordHash(PasswordUtil.hashPassword(user.getPasswordHash()));
        repository.save(user);
    }

    public String loginUser(String identifier, String password) {
        Optional<User> existingUser;
        if(ValidateEmail.isEmail(identifier)){
            existingUser = repository.findByEmail(identifier);
        }
        else{
            existingUser = repository.findByUserName(identifier);
        }

        if(!existingUser.isPresent()){
            throw new InvalidUserCredentialsException("Invalid input credentials. Please try again");
        }

        User user = existingUser.get();

        if(!PasswordUtil.matches(password, user.getPasswordHash())){
            throw new InvalidUserCredentialsException("Invalid input credentials. Please try again");
        }

        Dotenv dotenv = Dotenv.load();
        String secret = dotenv.get("JWT_SECRET");
        long expiry = Long.parseLong(dotenv.get("JWT_EXPIRY"));

        // Generate JWT token
        Algorithm jwtAlgorithm = Algorithm.HMAC256(secret); // Using HMAC256 as the algorithm
        String token = JWT.create()
                .withSubject(user.getUserId().toString()) // Use a unique identifier for the user
                .withClaim("email", user.getEmail())
                .withClaim("username", user.getUserName())
                .withIssuedAt(new Date()) // Current time
                .withExpiresAt(new Date(System.currentTimeMillis() + expiry)) // Expiry time
                .sign(jwtAlgorithm); // Sign the token with the secret key

        return token; 

    }

    public void updateProfile(String userId, String newEmail, String newUserName) {
        // Find the user by ID
        Optional<User> optionalUser = repository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
    
        User user = optionalUser.get();
    
        // Validate the new email
        if (!ValidateEmail.isEmail(newEmail)) {
            throw new InvalidUserCredentialsException("Invalid email format!");
        }
    
        // Check if the new email is already in use by another user
        Optional<User> checkUserEmail = repository.findByEmail(newEmail);
        if (checkUserEmail.isPresent() && !checkUserEmail.get().getUserId().equals(userId)) {
            throw new UserAlreadyExistsException("Email is already in use by another user!");
        }
    
        // Check if the new username is already in use by another user
        Optional<User> checkUserUsername = repository.findByUserName(newUserName);
        if (checkUserUsername.isPresent() && !checkUserUsername.get().getUserId().equals(userId)) {
            throw new UserAlreadyExistsException("Username is already taken by another user!");
        }
    
        // Update the user's email and username
        user.setEmail(newEmail);
        user.setUserName(newUserName);
    
        // Save the updated user
        repository.save(user);
    }
    
    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public void changePassword(String userId, String newPassword) {
        // Find the user by ID
        Optional<User> optionalUser = repository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
    
        // Update the password
        User user = optionalUser.get();
        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
    
        // Save the updated user
        repository.save(user);
    }
}
