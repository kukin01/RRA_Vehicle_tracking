
package com.rra.vehicle.dto.history;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OwnershipHistoryResponse {
    private Long id;
    private String vehicleChassisNumber;
    private String vehicleModel;
    private String ownerName;
    private String ownerNationalId;
    private String plateNumber;
    private BigDecimal purchasePrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean currentOwner;
}
