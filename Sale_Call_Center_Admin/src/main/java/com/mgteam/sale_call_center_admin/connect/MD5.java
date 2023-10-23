/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_admin.connect;

import java.security.MessageDigest;
import java.util.Base64;

/**
 *
 * @author tranp
 */
public class MD5 {
    public static String encryPassword(String input) {
        try {
            String base64Encode = Base64.getEncoder().encodeToString(input.getBytes());
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Byte = md.digest(base64Encode.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : md5Byte) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
   
}
