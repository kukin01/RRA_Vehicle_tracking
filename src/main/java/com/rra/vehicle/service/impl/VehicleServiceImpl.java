package com.rra.vehicle.service.impl;

import com.rra.vehicle.dto.vehicle.VehicleRegistrationRequest;
import com.rra.vehicle.dto.vehicle.VehicleResponse;
import com.rra.vehicle.dto.vehicle.VehicleTransferRequest;
import com.rra.vehicle.exception.ResourceAlreadyExistsException;
import com.rra.vehicle.exception.ResourceNotFoundException;
import com.rra.vehicle.model.OwnershipHistory;
import com.rra.vehicle.model.PlateNumber;
import com.rra.vehicle.model.Vehicle;
import com.rra.vehicle.model.VehicleOwner;
import com.rra.vehicle.repository.OwnershipHistoryRepository;
import com.rra.vehicle.repository.VehicleRepository;
import com.rra.vehicle.service.PlateNumberService;
import com.rra.vehicle.service.VehicleOwnerService;
import com.rra.vehicle.service.VehicleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class VehicleServiceImpl implements VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private VehicleOwnerService ownerService;
    
    @Autowired
    private PlateNumberService plateNumberService;
    
    @Autowired
    private OwnershipHistoryRepository ownershipHistoryRepository;
    
    @Override
    @Transactional
    public VehicleResponse registerVehicle(VehicleRegistrationRequest request) {
        if (vehicleRepository.existsByChassisNumber(request.getChassisNumber())) {
            throw new ResourceAlreadyExistsException("Vehicle with chassis number already exists: " + request.getChassisNumber());
        }
        
        VehicleOwner owner = ownerService.getOwnerEntityByNationalId(request.getOwnerNationalId());
        PlateNumber plateNumber = plateNumberService.getPlateNumberEntityByPlate(request.getPlateNumber());
        
        // Validate the plate belongs to the owner and is available
        if (!plateNumber.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Plate number does not belong to the specified owner");
        }
        
        if (plateNumber.getStatus() != PlateNumber.PlateStatus.AVAILABLE) {
            throw new IllegalArgumentException("Plate number is already in use");
        }
        
        // Create and save the vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setChassisNumber(request.getChassisNumber());
        vehicle.setManufactureCompany(request.getManufactureCompany());
        vehicle.setManufactureYear(request.getManufactureYear());
        vehicle.setPrice(request.getPrice());
        vehicle.setModelName(request.getModelName());
        vehicle.setCurrentOwner(owner);
        vehicle.setCurrentPlate(plateNumber);
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        
        // Mark plate number as in use
        plateNumberService.markPlateAsInUse(plateNumber);
        
        // Create ownership history
        OwnershipHistory history = new OwnershipHistory();
        history.setVehicle(savedVehicle);
        history.setOwner(owner);
        history.setPlateNumber(plateNumber);
        history.setPurchasePrice(request.getPrice());
        history.setStartDate(LocalDateTime.now());
        history.setCurrentOwner(true);
        ownershipHistoryRepository.save(history);
        
        logger.info("Vehicle registered successfully: {}", savedVehicle.getChassisNumber());
        
        return mapToVehicleResponse(savedVehicle);
    }
    
    @Override
    @Transactional
    public VehicleResponse transferVehicle(VehicleTransferRequest request) {
        Vehicle vehicle = vehicleRepository.findByChassisNumber(request.getChassisNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with chassis number: " + request.getChassisNumber()));
        
        VehicleOwner newOwner = ownerService.getOwnerEntityByNationalId(request.getNewOwnerNationalId());
        
        if (vehicle.getCurrentOwner().getNationalId().equals(newOwner.getNationalId())) {
            throw new IllegalArgumentException("Vehicle already belongs to this owner");
        }
        
        // Get an available plate from the new owner
        PlateNumber newPlate = plateNumberService.getAvailablePlateForOwner(newOwner.getNationalId());
        
        // Mark the old ownership history as ended
        OwnershipHistory currentOwnership = ownershipHistoryRepository.findByVehicleOrderByStartDateDesc(vehicle)
                .stream()
                .filter(OwnershipHistory::isCurrentOwner)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Current ownership record not found"));
        
        currentOwnership.setEndDate(LocalDateTime.now());
        currentOwnership.setCurrentOwner(false);
        ownershipHistoryRepository.save(currentOwnership);
        
        // Mark the old plate as available
        plateNumberService.markPlateAsAvailable(vehicle.getCurrentPlate());
        
        // Create new ownership history
        OwnershipHistory newOwnership = new OwnershipHistory();
        newOwnership.setVehicle(vehicle);
        newOwnership.setOwner(newOwner);
        newOwnership.setPlateNumber(newPlate);
        newOwnership.setPurchasePrice(request.getPurchasePrice());
        newOwnership.setStartDate(LocalDateTime.now());
        newOwnership.setCurrentOwner(true);
        ownershipHistoryRepository.save(newOwnership);
        
        // Update vehicle with new owner and plate
        vehicle.setCurrentOwner(newOwner);
        vehicle.setCurrentPlate(newPlate);
        vehicle.setPrice(request.getPurchasePrice()); // Update current value of the vehicle
        
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        
        // Mark the new plate as in use
        plateNumberService.markPlateAsInUse(newPlate);
        
        logger.info("Vehicle transferred successfully: {} from {} to {}", 
                updatedVehicle.getChassisNumber(), 
                currentOwnership.getOwner().getNationalId(),
                newOwner.getNationalId());
        
        return mapToVehicleResponse(updatedVehicle);
    }
    
    @Override
    public VehicleResponse getVehicleByChassisNumber(String chassisNumber) {
        Vehicle vehicle = vehicleRepository.findByChassisNumber(chassisNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with chassis number: " + chassisNumber));
        return mapToVehicleResponse(vehicle);
    }
    
    @Override
    public VehicleResponse getVehicleByPlateNumber(String plateNumber) {
        PlateNumber plate = plateNumberService.getPlateNumberEntityByPlate(plateNumber);
        Vehicle vehicle = vehicleRepository.findByCurrentPlate(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with plate number: " + plateNumber));
        return mapToVehicleResponse(vehicle);
    }
    
    @Override
    public Page<VehicleResponse> getVehiclesByOwnerNationalId(String nationalId, Pageable pageable) {
        VehicleOwner owner = ownerService.getOwnerEntityByNationalId(nationalId);
        return vehicleRepository.findByCurrentOwner(owner, pageable).map(this::mapToVehicleResponse);
    }
    
    @Override
    public Page<VehicleResponse> getAllVehicles(Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(this::mapToVehicleResponse);
    }
    
    @Override
    public Vehicle getVehicleEntityByChassisNumber(String chassisNumber) {
        return vehicleRepository.findByChassisNumber(chassisNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with chassis number: " + chassisNumber));
    }
    
    @Override
    public boolean vehicleExists(String chassisNumber) {
        return vehicleRepository.existsByChassisNumber(chassisNumber);
    }
    
    private VehicleResponse mapToVehicleResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setChassisNumber(vehicle.getChassisNumber());
        response.setManufactureCompany(vehicle.getManufactureCompany());
        response.setManufactureYear(vehicle.getManufactureYear());
        response.setPrice(vehicle.getPrice());
        response.setModelName(vehicle.getModelName());
        response.setRegistrationDate(vehicle.getRegistrationDate());
        response.setOwnerName(vehicle.getCurrentOwner().getFullName());
        response.setOwnerNationalId(vehicle.getCurrentOwner().getNationalId());
        response.setPlateNumber(vehicle.getCurrentPlate().getPlateNumber());
        return response;
    }
}
