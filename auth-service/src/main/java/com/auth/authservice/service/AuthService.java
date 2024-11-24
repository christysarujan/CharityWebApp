package com.auth.authservice.service;

import com.auth.authservice.dto.LoginDto;
import com.auth.authservice.entity.User;
import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.security.JwtTokenProvider;
import com.auth.authservice.util.AdminVerificationStatus;
import com.auth.authservice.util.OTPGenerator;
import com.auth.authservice.util.Role;
import com.auth.authservice.util.VacationStatus;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService {

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    private UserRepository userRepository;


    private PasswordEncoder passwordEncoder;
    private OTPGenerator otpGenerator;
    private EmailService emailService;

    //Store generated OTPs temporarily
    private final Map<String, String> adminOtpCache = new HashMap<>();

    public User registerUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Username already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVacationStatus(VacationStatus.IN_COUNTRY);

        if(Role.ADMIN.equals(user.getRoles())){
            user.setVerificationStatus(AdminVerificationStatus.PENDING);
        }

        return userRepository.save(user);
    }

    public String login(LoginDto loginDto){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));
        System.out.println(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        System.out.println(token);
        return token;
    }

    public String generateAdminSecret(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Role.ADMIN.equals(user.getRoles())) {
            throw new RuntimeException("User is not an admin");
        }

        String otp = otpGenerator.generateOTP();
        adminOtpCache.put(username, otp); // Temporarily store the OTP for verification
        return otp; // Return the OTP to be shown on the frontend
    }


    public String updateAdminVerificationStatus(String username, String adminVerificationCode){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!Role.ADMIN.equals(user.getRoles())){
            throw new RuntimeException("User is not an admin");
        }

        String cachedOtp = adminOtpCache.get(username);
        if(cachedOtp == null || !cachedOtp.equals(adminVerificationCode)){
            throw new RuntimeException("Invalid Verification code");
        }
        user.setVerificationStatus(AdminVerificationStatus.VERIFIED);
        userRepository.save(user);

        return "Admin verification successful";
    }
}
