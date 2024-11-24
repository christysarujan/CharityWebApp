package com.auth.authservice.entity;

import com.auth.authservice.util.AdminVerificationStatus;
import com.auth.authservice.util.Role;
import com.auth.authservice.util.VacationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String addressLine01;
    private String addressLine02;
    private String countryCode;
    private String mobileNumber;
    @Enumerated(EnumType.STRING)
    private VacationStatus vacationStatus;
    @Enumerated(EnumType.STRING)
    private Role roles;
    @Enumerated(EnumType.STRING)
    private AdminVerificationStatus verificationStatus;


}
