/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager.connect.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tranp
 */
public class validate {
    public static boolean validateEmail(String email){
        String regex="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean validatePhoneNumber(String phoneNumber){
        String regex="^\\+?[0-9. ()-]{10}$";
        Pattern pattern=Pattern.compile(regex);
       Matcher matcher=pattern.matcher(phoneNumber);
       if(phoneNumber.matches("0{10}")|| phoneNumber.matches("1{10}")||phoneNumber.matches("2{10}")||phoneNumber.matches("3{10}")||phoneNumber.matches("4{10}")||phoneNumber.matches("5{10}")||phoneNumber.matches("6{10}")||phoneNumber.matches("7{10}")|| phoneNumber.matches("8{10}")||phoneNumber.matches("9{10}")){
           return false;    
       }
       return matcher.matches();
    }
    public static boolean validateUsername(String username){
        String regex="^[a-zA-Z0-9_]{6,15}$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(username);
        return matcher.matches();
    }
}
