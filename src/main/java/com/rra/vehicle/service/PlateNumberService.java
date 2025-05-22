
package com.rra.vehicle.service;

import com.rra.vehicle.dto.plate.PlateNumberRequest;
import com.rra.vehicle.dto.plate.PlateNumberResponse;
import com.rra.vehicle.model.PlateNumber;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlateNumberService {
    PlateNumberResponse registerPlateNumber(PlateNumberRequest plateNumberRequest);
    Page<PlateNumberResponse> getPlateNumbersByOwner(String nationalId, Pageable pageable);
    PlateNumberResponse getPlateNumberByPlate(String plateNumber);
    PlateNumber getAvailablePlateForOwner(String nationalId);
    void markPlateAsInUse(PlateNumber plateNumber);
    void markPlateAsAvailable(PlateNumber plateNumber);
    boolean plateNumberExists(String plateNumber);
    PlateNumber getPlateNumberEntityByPlate(String plateNumber);
}
