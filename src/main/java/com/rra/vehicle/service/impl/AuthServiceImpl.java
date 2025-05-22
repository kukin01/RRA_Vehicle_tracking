
package com.rra.vehicle.service.impl;

import com.rra.vehicle.dto.auth.JwtResponse;
import com.rra.vehicle.dto.auth.LoginRequest;
import com.rra.vehicle.dto.auth.SignupRequest;
import com.rra.vehicle.exception.ResourceAlreadyExistsException;
import com.rra.vehicle.exception.ResourceNotFoundException;
import com.rra.vehicle.model.Role;
import com.rra.vehicle.model.User;
import com.rra.vehicle.repository.UserRepository;
import com.rra.vehicle.security.JwtUtils;
import com.rra.vehicle.security.UserDetailsImpl;
import com.rra.vehicle.service.AuthService;
import com.rra.vehicle.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public void registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already in use!");
        }

        if (userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
            throw new ResourceAlreadyExistsException("Phone number is already in use!");
        }

        if (userRepository.existsByNationalId(signupRequest.getNationalId())) {
            throw new ResourceAlreadyExistsException("National ID is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setEmail(signupRequest.getEmail());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setNationalId(signupRequest.getNationalId());
        user.setPassword(encoder.encode(signupRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_STANDARD);
        
        if (signupRequest.getIsAdmin() != null && signupRequest.getIsAdmin()) {
            roles.add(Role.ROLE_ADMIN);
        }

        user.setRoles(roles);
        
        // Generate verification code
        String verificationCode = generateRandomCode();
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiryTime(LocalDateTime.now().plusMinutes(30));
        
        // Save user
        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());
        
        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), user.getFullName(), verificationCode);
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        
        if (!userDetails.isVerified()) {
            throw new IllegalStateException("Account not verified. Please verify your email first.");
        }

        return new JwtResponse(jwt, 
                               userDetails.getId(), 
                               userDetails.getUsername(), 
                               userDetails.getFullName(),
                               roles);
    }

    @Override
    @Transactional
    public void verifyAccount(String code) {
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification code"));

        if (user.getVerificationCodeExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Verification code has expired. Please request a new one.");
        }
        
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiryTime(null);
        userRepository.save(user);
        
        logger.info("User verified successfully: {}", user.getEmail());
        
        // Send success email
        emailService.sendSuccessEmail(
            user.getEmail(),
            user.getFullName(),
            "Account Verified Successfully",
            "Your account has been verified successfully. You can now login to the RRA Vehicle Tracking System."
        );
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        String resetCode = generateRandomCode();
        user.setResetPasswordCode(resetCode);
        user.setResetPasswordCodeExpiryTime(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);
        
        logger.info("Password reset requested for user: {}", user.getEmail());
        
        // Send password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), resetCode);
    }

    @Override
    @Transactional
    public void resetPassword(String code, String newPassword) {
        User user = userRepository.findByResetPasswordCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid reset code"));

        if (user.getResetPasswordCodeExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reset code has expired. Please request a new one.");
        }
        
        user.setPassword(encoder.encode(newPassword));
        user.setResetPasswordCode(null);
        user.setResetPasswordCodeExpiryTime(null);
        userRepository.save(user);
        
        logger.info("Password reset successful for user: {}", user.getEmail());
        
        // Send success email
        emailService.sendSuccessEmail(
            user.getEmail(),
            user.getFullName(),
            "Password Reset Successful",
            "Your password has been reset successfully. You can now login with your new password."
        );
    }
    
    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }
}
