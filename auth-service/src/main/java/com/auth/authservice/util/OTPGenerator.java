package com.auth.authservice.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
@Component
public class OTPGenerator {

    private static final String DIGITS = "0123456789";


    public String generateOTP(){
        int length = 6;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for(int i = 0; i < length; i++){
            int index = secureRandom.nextInt(DIGITS.length());
            otp.append(DIGITS.charAt(index));
        }
        return otp.toString();
    }
}
