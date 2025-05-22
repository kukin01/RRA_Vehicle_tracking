
package com.rra.vehicle.controller;

import com.rra.vehicle.dto.history.OwnershipHistoryResponse;
import com.rra.vehicle.service.OwnershipHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@Tag(name = "Vehicle Ownership History", description = "Vehicle Ownership History API")
@SecurityRequirement(name = "bearerAuth")
public class OwnershipHistoryController {
    
    @Autowired
    private OwnershipHistoryService historyService;
    
    @GetMapping("/vehicle")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get vehicle ownership history by chassis number or plate number")
    public ResponseEntity<List<OwnershipHistoryResponse>> getVehicleHistory(
            @RequestParam String type,
            @RequestParam String identifier) {
        
        List<OwnershipHistoryResponse> history = 
                historyService.getVehicleOwnershipHistory(type, identifier);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/vehicle/paginated")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get paginated vehicle ownership history by chassis number or plate number")
    public ResponseEntity<Page<OwnershipHistoryResponse>> getVehicleHistoryPaginated(
            @RequestParam String type,
            @RequestParam String identifier,
            Pageable pageable) {
        
        Page<OwnershipHistoryResponse> history = 
                historyService.getVehicleOwnershipHistoryPaginated(type, identifier, pageable);
        return ResponseEntity.ok(history);
    }
}
