package com.BudgetEase.utils;

import java.util.regex.Pattern;

public class ValidateEmail {
    
    public static boolean isEmail(String mail){
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(mail).matches();
    }
}
