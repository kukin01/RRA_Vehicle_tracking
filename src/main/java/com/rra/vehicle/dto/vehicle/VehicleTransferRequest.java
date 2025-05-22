
package com.rra.vehicle.dto.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleTransferRequest {
    
    @NotBlank(message = "Chassis number is required")
    private String chassisNumber;
    
    @NotBlank(message = "New owner national ID is required")
    private String newOwnerNationalId;
    
    @NotNull(message = "Purchase price is required")
    @Min(value = 0, message = "Purchase price must be a positive number")
    private BigDecimal purchasePrice;
}
