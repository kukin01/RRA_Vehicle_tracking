
package com.rra.vehicle.dto.owner;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OwnerResponse {
    private Long id;
    private String fullName;
    private String nationalId;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
}
