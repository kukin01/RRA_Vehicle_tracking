
package com.rra.vehicle.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "plate_numbers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlateNumber {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Plate number is required")
    @Pattern(regexp = "^[A-Z]{2}\\d{3}[A-Z]{1,2}$", message = "Plate number format should be like RAB123A")
    @Column(unique = true)
    private String plateNumber;
    
    @NotNull(message = "Issue date is required")
    private LocalDateTime issuedDate;
    
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private VehicleOwner owner;
    
    @Enumerated(EnumType.STRING)
    private PlateStatus status = PlateStatus.AVAILABLE;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum PlateStatus {
        AVAILABLE,
        IN_USE
    }
}
