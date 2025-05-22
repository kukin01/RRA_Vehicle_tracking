
package com.rra.vehicle.controller;

import com.rra.vehicle.dto.auth.*;
import com.rra.vehicle.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        authService.registerUser(signupRequest);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully! Please check your email for verification code.");
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/signin")
    @Operation(summary = "Authenticate a user and return a JWT token")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwt = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwt);
    }
    
    @PostMapping("/verify")
    @Operation(summary = "Verify a user account with verification code")
    public ResponseEntity<Map<String, String>> verifyUser(@RequestParam String code) {
        authService.verifyAccount(code);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account verified successfully!");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/request-reset")
    @Operation(summary = "Request a password reset")
    public ResponseEntity<Map<String, String>> requestReset(@Valid @RequestBody PasswordResetRequest request) {
        authService.requestPasswordReset(request.getEmail());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset instructions sent to your email.");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using code sent to email")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody PasswordUpdateRequest request) {
        authService.resetPassword(request.getResetCode(), request.getNewPassword());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully!");
        
        return ResponseEntity.ok(response);
    }
}
