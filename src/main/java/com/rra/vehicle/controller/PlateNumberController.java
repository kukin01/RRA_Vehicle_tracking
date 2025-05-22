
package com.rra.vehicle.controller;

import com.rra.vehicle.dto.plate.PlateNumberRequest;
import com.rra.vehicle.dto.plate.PlateNumberResponse;
import com.rra.vehicle.service.PlateNumberService;

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
@RequestMapping("/api/plates")
@Tag(name = "Plate Numbers", description = "Plate Numbers API")
@SecurityRequirement(name = "bearerAuth")
public class PlateNumberController {
    
    @Autowired
    private PlateNumberService plateNumberService;
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Register a new plate number")
    public ResponseEntity<PlateNumberResponse> registerPlateNumber(@Valid @RequestBody PlateNumberRequest request) {
        PlateNumberResponse response = plateNumberService.registerPlateNumber(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/owner/{nationalId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all plate numbers for a specific owner (paginated)")
    public ResponseEntity<Page<PlateNumberResponse>> getPlatesByOwner(
            @PathVariable String nationalId, Pageable pageable) {
        Page<PlateNumberResponse> plates = plateNumberService.getPlateNumbersByOwner(nationalId, pageable);
        return ResponseEntity.ok(plates);
    }
    
    @GetMapping("/{plateNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get plate number details by plate number")
    public ResponseEntity<PlateNumberResponse> getPlateByNumber(@PathVariable String plateNumber) {
        PlateNumberResponse response = plateNumberService.getPlateNumberByPlate(plateNumber);
        return ResponseEntity.ok(response);
    }
}
