
package com.rra.vehicle.service;

import com.rra.vehicle.dto.auth.LoginRequest;
import com.rra.vehicle.dto.auth.SignupRequest;
import com.rra.vehicle.dto.auth.JwtResponse;

public interface AuthService {
    void registerUser(SignupRequest signupRequest);
    JwtResponse authenticateUser(LoginRequest loginRequest);
    void verifyAccount(String code);
    void requestPasswordReset(String email);
    void resetPassword(String code, String newPassword);
}
