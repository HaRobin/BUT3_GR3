package com.iut.banque.utils;

import com.iut.banque.exceptions.PasswordHashingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    private PasswordUtils() {
        // Private constructor to prevent instantiation
    }

    public static String hashPwd(String userPwd) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Update the digest with the byte representation of the password
            byte[] hashedBytes = digest.digest(userPwd.getBytes());

            // Convert the byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Return the hexadecimal string
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new PasswordHashingException("Error hashing password", e);
        }
    }
}
