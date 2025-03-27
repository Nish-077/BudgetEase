package com.BudgetEase.BudgetEaseService;


import com.BudgetEase.Exceptions.InvalidUserCredentialsException;
import com.BudgetEase.Exceptions.UserAlreadyExistsException;
import com.BudgetEase.Models.User;
import com.BudgetEase.repository.BudgetEaseRepository;
import com.BudgetEase.utils.PasswordUtil;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
public class UserService {
    private final BudgetEaseRepository repository;

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

    public boolean isEmail(String identifier) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(identifier).matches();
    }

    public void loginUser(String identifier, String password) {
        Optional<User> existingUser;
        if(isEmail(identifier)){
            existingUser = repository.findByEmail(identifier);
        }
        else{
            existingUser = repository.findByUserName(identifier);
        }

        if(!existingUser.isPresent()){
            throw new InvalidUserCredentialsException("Invalid input credentials. Please try again");
        }

        if(!PasswordUtil.matches(password, existingUser.get().getPasswordHash())){
            throw new InvalidUserCredentialsException("Invalid input credentials. Please try again");
        }

    }

    public void updateProfile(User updatedUser) {

    }

    public void changePassword(String newPassword) {

    }

}
