
package com.rra.vehicle.controller;

import com.rra.vehicle.dto.vehicle.VehicleRegistrationRequest;
import com.rra.vehicle.dto.vehicle.VehicleResponse;
import com.rra.vehicle.dto.vehicle.VehicleTransferRequest;
import com.rra.vehicle.service.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "Vehicles API")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Register a new vehicle")
    public ResponseEntity<VehicleResponse> registerVehicle(@Valid @RequestBody VehicleRegistrationRequest request) {
        VehicleResponse response = vehicleService.registerVehicle(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Transfer vehicle ownership")
    public ResponseEntity<VehicleResponse> transferVehicle(@Valid @RequestBody VehicleTransferRequest request) {
        VehicleResponse response = vehicleService.transferVehicle(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all vehicles (paginated)")
    public ResponseEntity<Page<VehicleResponse>> getAllVehicles(Pageable pageable) {
        Page<VehicleResponse> vehicles = vehicleService.getAllVehicles(pageable);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/chassis/{chassisNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get vehicle by chassis number")
    public ResponseEntity<VehicleResponse> getVehicleByChassisNumber(@PathVariable String chassisNumber) {
        VehicleResponse response = vehicleService.getVehicleByChassisNumber(chassisNumber);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/plate/{plateNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get vehicle by plate number")
    public ResponseEntity<VehicleResponse> getVehicleByPlateNumber(@PathVariable String plateNumber) {
        VehicleResponse response = vehicleService.getVehicleByPlateNumber(plateNumber);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/owner/{nationalId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get vehicles by owner national ID (paginated)")
    public ResponseEntity<Page<VehicleResponse>> getVehiclesByOwnerNationalId(
            @PathVariable String nationalId, Pageable pageable) {
        Page<VehicleResponse> vehicles = vehicleService.getVehiclesByOwnerNationalId(nationalId, pageable);
        return ResponseEntity.ok(vehicles);
    }
}
