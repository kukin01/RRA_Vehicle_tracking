
package com.rra.vehicle.service;

import com.rra.vehicle.dto.owner.OwnerRequest;
import com.rra.vehicle.dto.owner.OwnerResponse;
import com.rra.vehicle.model.VehicleOwner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleOwnerService {
    OwnerResponse registerOwner(OwnerRequest ownerRequest);
    Page<OwnerResponse> getAllOwners(Pageable pageable);
    OwnerResponse getOwnerById(Long id);
    OwnerResponse getOwnerByNationalId(String nationalId);
    VehicleOwner getOwnerEntityByNationalId(String nationalId);
    boolean ownerExists(String nationalId);
}
