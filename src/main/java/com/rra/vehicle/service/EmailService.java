
package com.rra.vehicle.service;

public interface EmailService {
    void sendVerificationEmail(String to, String name, String verificationCode);
    void sendPasswordResetEmail(String to, String name, String resetCode);
    void sendSuccessEmail(String to, String name, String message, String details);
}
