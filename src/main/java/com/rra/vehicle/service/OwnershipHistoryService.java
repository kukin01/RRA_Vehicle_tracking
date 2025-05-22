
package com.rra.vehicle.service;

import com.rra.vehicle.dto.history.OwnershipHistoryResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OwnershipHistoryService {
    List<OwnershipHistoryResponse> getVehicleOwnershipHistory(String identifierType, String identifier);
    Page<OwnershipHistoryResponse> getVehicleOwnershipHistoryPaginated(String identifierType, String identifier, Pageable pageable);
}
