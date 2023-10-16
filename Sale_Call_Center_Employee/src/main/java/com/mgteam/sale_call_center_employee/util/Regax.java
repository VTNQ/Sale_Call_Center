package com.mgteam.sale_call_center_employee.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regax {
    public static boolean isValidUsername(String username){
        String regex="^[a-zA-Z0-9_],{6,}$";
        Pattern pattern =Pattern.compile(regex);
        Matcher matcher =pattern.matcher(username);
        return matcher.matches();
    }
    public static boolean isValidPassword(String Password){
        String regex="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)[a-zA-Z\\\\d]{8,}$";
        Pattern pattern =Pattern.compile(regex);
        Matcher matcher =pattern.matcher(Password);
        return matcher.matches();
    }
}
