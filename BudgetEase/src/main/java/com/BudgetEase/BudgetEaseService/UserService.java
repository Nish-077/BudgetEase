package com.BudgetEase.BudgetEaseService;


import com.BudgetEase.Exceptions.InvalidUserCredentialsException;
import com.BudgetEase.Exceptions.UserAlreadyExistsException;
import com.BudgetEase.Models.Budget;
import com.BudgetEase.Models.User;
import com.BudgetEase.repository.UserRepository;
import com.BudgetEase.utils.GetCurrentUser;
import com.BudgetEase.utils.PasswordUtil;
import com.BudgetEase.utils.ValidateEmail;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    @SuppressWarnings("unchecked")
    public User registerUser(String email, String username, String password, String phoneNo) {
        Optional<User> registeredUser = repository.findByEmail(email);
        if(registeredUser.isPresent()){
            throw new UserAlreadyExistsException("Email already exists");
        }

        registeredUser = repository.findByUserName(username);
        if(registeredUser.isPresent()){
            throw new UserAlreadyExistsException("Username already taken");
        }

        User user = new User();
        user.setEmail(email);
        user.setUserName(username);
        user.setPhoneNumber(phoneNo);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        return repository.save(user);
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

    public void updateProfile(String userId, String newEmail, String newUserName, String profileUrl) {
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
        user.setProfilePictureUrl(profileUrl);
    
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

    public Optional<User> getUserById(String userId){
        return repository.findById(userId);
    }

    public Budget addBudget(Budget budget, String userId){
        User user = repository.findById(userId).orElseThrow( () -> new IllegalArgumentException("User with this id does not exist") );

        List<Budget> budgets = user.getBudgets();

        if(budgets == null){
            budgets=new ArrayList<>();
        }

        if(budgets.stream().anyMatch(existingBudget -> existingBudget.getBudgetId().equals(budget.getBudgetId()))){
            throw new IllegalArgumentException( "Budget with this id already exists for the user" );
        }

        budgets.add(budget);

        user.setBudgets(budgets);
        repository.save(user);

        return budget;
    }

    public List<Budget> listBudgets(String userId){
        User user = repository.findById(userId).orElseThrow( () -> new IllegalArgumentException("User id does not exist") );

        System.out.println(user.getBudgets());

        return user.getBudgets();
    }

    public void deleteBudget(String budgetId, String userId) {
        // Find the user by ID
        User user = repository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User id not found"));
    
        // Get the user's budgets
        List<Budget> budgets = user.getBudgets();
    
        // Check if the budgets list is null or empty
        if (budgets == null || budgets.isEmpty()) {
            throw new IllegalArgumentException("No budgets found for the user");
        }
    
        // Find the budget to delete
        Budget budgetToDelete = budgets.stream()
            .filter(budget -> budget.getBudgetId().equals(budgetId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Budget with this ID not found for the user"));
    
        // Remove the budget
        budgets.remove(budgetToDelete);
    
        // Update the user's budgets
        user.setBudgets(budgets);
    
        // Save the updated user
        repository.save(user);
    }

    public User findUserByEmailOrUserName(String email,String username){
        return repository.findByEmailOrUserName(email,username);
    }
}
