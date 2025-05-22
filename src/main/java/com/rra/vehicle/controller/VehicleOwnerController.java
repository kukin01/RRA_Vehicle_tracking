
package com.rra.vehicle.controller;

import com.rra.vehicle.dto.owner.OwnerRequest;
import com.rra.vehicle.dto.owner.OwnerResponse;
import com.rra.vehicle.service.VehicleOwnerService;

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
@RequestMapping("/api/owners")
@Tag(name = "Vehicle Owners", description = "Vehicle Owners API")
@SecurityRequirement(name = "bearerAuth")
public class VehicleOwnerController {
    
    @Autowired
    private VehicleOwnerService ownerService;
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Register a new vehicle owner")
    public ResponseEntity<OwnerResponse> registerOwner(@Valid @RequestBody OwnerRequest ownerRequest) {
        OwnerResponse response = ownerService.registerOwner(ownerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all vehicle owners (paginated)")
    public ResponseEntity<Page<OwnerResponse>> getAllOwners(Pageable pageable) {
        Page<OwnerResponse> owners = ownerService.getAllOwners(pageable);
        return ResponseEntity.ok(owners);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get vehicle owner by ID")
    public ResponseEntity<OwnerResponse> getOwnerById(@PathVariable Long id) {
        OwnerResponse owner = ownerService.getOwnerById(id);
        return ResponseEntity.ok(owner);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Search vehicle owner by National ID")
    public ResponseEntity<OwnerResponse> searchOwnerByNationalId(@RequestParam String nationalId) {
        OwnerResponse owner = ownerService.getOwnerByNationalId(nationalId);
        return ResponseEntity.ok(owner);
    }
}
