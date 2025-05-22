
package com.rra.vehicle.service.impl;

import com.rra.vehicle.dto.history.OwnershipHistoryResponse;
import com.rra.vehicle.model.OwnershipHistory;
import com.rra.vehicle.model.PlateNumber;
import com.rra.vehicle.model.Vehicle;
import com.rra.vehicle.repository.OwnershipHistoryRepository;
import com.rra.vehicle.service.OwnershipHistoryService;
import com.rra.vehicle.service.PlateNumberService;
import com.rra.vehicle.service.VehicleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnershipHistoryServiceImpl implements OwnershipHistoryService {

    @Autowired
    private OwnershipHistoryRepository historyRepository;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private PlateNumberService plateNumberService;

    @Override
    public List<OwnershipHistoryResponse> getVehicleOwnershipHistory(String identifierType, String identifier) {
        Vehicle vehicle;
        
        if ("chassis".equals(identifierType)) {
            vehicle = vehicleService.getVehicleEntityByChassisNumber(identifier);
        } else if ("plate".equals(identifierType)) {
            PlateNumber plateNumber = plateNumberService.getPlateNumberEntityByPlate(identifier);
            vehicle = vehicleService.getVehicleEntityByChassisNumber(
                    vehicleService.getVehicleByPlateNumber(identifier).getChassisNumber());
        } else {
            throw new IllegalArgumentException("Invalid identifier type. Use 'chassis' or 'plate'");
        }
        
        List<OwnershipHistory> historyList = historyRepository.findByVehicleOrderByStartDateDesc(vehicle);
        
        return historyList.stream()
                .map(this::mapToOwnershipHistoryResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<OwnershipHistoryResponse> getVehicleOwnershipHistoryPaginated(
            String identifierType, String identifier, Pageable pageable) {
        
        Vehicle vehicle;
        
        if ("chassis".equals(identifierType)) {
            vehicle = vehicleService.getVehicleEntityByChassisNumber(identifier);
        } else if ("plate".equals(identifierType)) {
            PlateNumber plateNumber = plateNumberService.getPlateNumberEntityByPlate(identifier);
            vehicle = vehicleService.getVehicleEntityByChassisNumber(
                    vehicleService.getVehicleByPlateNumber(identifier).getChassisNumber());
        } else {
            throw new IllegalArgumentException("Invalid identifier type. Use 'chassis' or 'plate'");
        }
        
        return historyRepository.findByVehicle(vehicle, pageable)
                .map(this::mapToOwnershipHistoryResponse);
    }
    
    private OwnershipHistoryResponse mapToOwnershipHistoryResponse(OwnershipHistory history) {
        OwnershipHistoryResponse response = new OwnershipHistoryResponse();
        response.setId(history.getId());
        response.setVehicleChassisNumber(history.getVehicle().getChassisNumber());
        response.setVehicleModel(history.getVehicle().getModelName());
        response.setOwnerName(history.getOwner().getFullName());
        response.setOwnerNationalId(history.getOwner().getNationalId());
        response.setPlateNumber(history.getPlateNumber().getPlateNumber());
        response.setPurchasePrice(history.getPurchasePrice());
        response.setStartDate(history.getStartDate());
        response.setEndDate(history.getEndDate());
        response.setCurrentOwner(history.isCurrentOwner());
        return response;
    }
}
