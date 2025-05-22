
package com.rra.vehicle.dto.vehicle;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VehicleResponse {
    private Long id;
    private String chassisNumber;
    private String manufactureCompany;
    private Integer manufactureYear;
    private BigDecimal price;
    private String modelName;
    private LocalDateTime registrationDate;
    private String ownerName;
    private String ownerNationalId;
    private String plateNumber;
}
