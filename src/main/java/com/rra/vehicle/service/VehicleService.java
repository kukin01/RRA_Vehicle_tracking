
package com.rra.vehicle.service;

import com.rra.vehicle.dto.vehicle.VehicleRegistrationRequest;
import com.rra.vehicle.dto.vehicle.VehicleResponse;
import com.rra.vehicle.dto.vehicle.VehicleTransferRequest;
import com.rra.vehicle.model.Vehicle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleService {
    VehicleResponse registerVehicle(VehicleRegistrationRequest request);
    VehicleResponse transferVehicle(VehicleTransferRequest request);
    VehicleResponse getVehicleByChassisNumber(String chassisNumber);
    VehicleResponse getVehicleByPlateNumber(String plateNumber);
    Page<VehicleResponse> getVehiclesByOwnerNationalId(String nationalId, Pageable pageable);
    Page<VehicleResponse> getAllVehicles(Pageable pageable);
    Vehicle getVehicleEntityByChassisNumber(String chassisNumber);
    boolean vehicleExists(String chassisNumber);
}
