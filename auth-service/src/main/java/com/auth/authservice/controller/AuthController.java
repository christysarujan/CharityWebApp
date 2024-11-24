package com.auth.authservice.controller;

import com.auth.authservice.dto.JwtAuthResponse;
import com.auth.authservice.dto.LoginDto;
import com.auth.authservice.entity.User;
import com.auth.authservice.service.AuthService;
import com.auth.authservice.util.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        try {
            User newUser = authService.registerUser(user);
            if(Role.ADMIN.equals(newUser.getRoles())){
                String adminSecret = authService.generateAdminSecret(newUser.getUsername());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Admin registered successfully! Admin Secret: " + adminSecret);
            }
            return  ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        System.out.println(loginDto);
        String token = authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PostMapping("/verify-admin")
    public ResponseEntity<?> verifyAdminCode(@RequestParam String username, @RequestParam String adminCode) {
        try {
            String message = authService.updateAdminVerificationStatus(username, adminCode);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
