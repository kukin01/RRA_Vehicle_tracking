
package com.rra.vehicle.dto.plate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class PlateNumberRequest {
    
    @NotBlank(message = "Plate number is required")
    @Pattern(regexp = "^[A-Z]{2}\\d{3}[A-Z]{1,2}$", message = "Plate number format should be like RAB123A")
    private String plateNumber;
    
    @NotBlank(message = "Owner national ID is required")
    @Size(min = 16, max = 16, message = "National ID must be 16 characters")
    private String ownerNationalId;
}
