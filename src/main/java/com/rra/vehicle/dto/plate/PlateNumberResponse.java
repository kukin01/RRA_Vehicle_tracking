
package com.rra.vehicle.dto.plate;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlateNumberResponse {
    private Long id;
    private String plateNumber;
    private LocalDateTime issuedDate;
    private String status;
    private String ownerName;
    private String ownerNationalId;
}
