
package com.rra.vehicle.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Chassis number is required")
    @Column(unique = true)
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
    
    @ManyToOne
    @JoinColumn(name = "current_owner_id")
    private VehicleOwner currentOwner;
    
    @OneToOne
    @JoinColumn(name = "current_plate_id")
    private PlateNumber currentPlate;
    
    private LocalDateTime registrationDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.registrationDate = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
