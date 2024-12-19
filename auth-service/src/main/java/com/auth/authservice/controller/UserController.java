package com.auth.authservice.controller;

import com.auth.authservice.entity.User;
import com.auth.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal User user){
        System.out.println(user );
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUsername(username));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
