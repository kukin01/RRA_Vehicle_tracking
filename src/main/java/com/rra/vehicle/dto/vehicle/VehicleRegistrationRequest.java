
package com.rra.vehicle.dto.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleRegistrationRequest {
    
    @NotBlank(message = "Chassis number is required")
    private String chassisNumber;
    
    @NotBlank(message = "Manufacturer company is required")
    private String manufactureCompany;
    
    @NotNull(message = "Manufacture year is required")
    @Min(value = 1900, message = "Manufacture year must be valid")
    private Integer manufactureYear;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be a positive number")
    private BigDecimal price;
    
    @NotBlank(message = "Model name is required")
    private String modelName;
    
    @NotBlank(message = "Owner national ID is required")
    private String ownerNationalId;
    
    @NotBlank(message = "Plate number is required")
    @Pattern(regexp = "^[A-Z]{2}\\d{3}[A-Z]{1,2}$", message = "Plate number format should be like RAB123A")
    private String plateNumber;
}
