package com.BudgetEase.utils;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.BudgetEase.BudgetEaseService.UserService;
import com.BudgetEase.Models.User;

public class GetCurrentUser {

    final UserService userService;

    public GetCurrentUser(UserService userService){
        this.userService=userService;
    }

    public User obtainUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String token = (String) authentication.getPrincipal();

        String userId = JwtUtil.getUserIdFromToken(token);

        System.out.println("User id IN GETUSER = "+userId);

        Optional<User> checkUser = userService.getUserById(userId);

        if(!checkUser.isPresent()){
            return null;
        }

        return checkUser.get();
    }

}
