
package com.rra.vehicle.service.impl;

import com.rra.vehicle.dto.plate.PlateNumberRequest;
import com.rra.vehicle.dto.plate.PlateNumberResponse;
import com.rra.vehicle.exception.ResourceAlreadyExistsException;
import com.rra.vehicle.exception.ResourceNotFoundException;
import com.rra.vehicle.model.PlateNumber;
import com.rra.vehicle.model.VehicleOwner;
import com.rra.vehicle.repository.PlateNumberRepository;
import com.rra.vehicle.service.PlateNumberService;
import com.rra.vehicle.service.VehicleOwnerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlateNumberServiceImpl implements PlateNumberService {

    private static final Logger logger = LoggerFactory.getLogger(PlateNumberServiceImpl.class);
    
    @Autowired
    private PlateNumberRepository plateNumberRepository;
    
    @Autowired
    private VehicleOwnerService ownerService;
    
    @Override
    @Transactional
    public PlateNumberResponse registerPlateNumber(PlateNumberRequest plateNumberRequest) {
        if (plateNumberRepository.existsByPlateNumber(plateNumberRequest.getPlateNumber())) {
            throw new ResourceAlreadyExistsException("Plate number already exists: " + plateNumberRequest.getPlateNumber());
        }
        
        VehicleOwner owner = ownerService.getOwnerEntityByNationalId(plateNumberRequest.getOwnerNationalId());
        
        PlateNumber plateNumber = new PlateNumber();
        plateNumber.setPlateNumber(plateNumberRequest.getPlateNumber().toUpperCase());
        plateNumber.setIssuedDate(LocalDateTime.now());
        plateNumber.setOwner(owner);
        plateNumber.setStatus(PlateNumber.PlateStatus.AVAILABLE);
        
        PlateNumber savedPlateNumber = plateNumberRepository.save(plateNumber);
        logger.info("Plate number registered: {}", savedPlateNumber.getPlateNumber());
        
        return mapToPlateNumberResponse(savedPlateNumber);
    }
    
    @Override
    public Page<PlateNumberResponse> getPlateNumbersByOwner(String nationalId, Pageable pageable) {
        VehicleOwner owner = ownerService.getOwnerEntityByNationalId(nationalId);
        return plateNumberRepository.findByOwner(owner, pageable).map(this::mapToPlateNumberResponse);
    }
    
    @Override
    public PlateNumberResponse getPlateNumberByPlate(String plateNumber) {
        PlateNumber plate = plateNumberRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Plate number not found: " + plateNumber));
        return mapToPlateNumberResponse(plate);
    }
    
    @Override
    public PlateNumber getPlateNumberEntityByPlate(String plateNumber) {
        return plateNumberRepository.findByPlateNumber(plateNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Plate number not found: " + plateNumber));
    }
    
    @Override
    @Transactional
    public PlateNumber getAvailablePlateForOwner(String nationalId) {
        VehicleOwner owner = ownerService.getOwnerEntityByNationalId(nationalId);
        List<PlateNumber> availablePlates = plateNumberRepository.findByOwnerAndStatus(owner, PlateNumber.PlateStatus.AVAILABLE);
        
        if (availablePlates.isEmpty()) {
            throw new ResourceNotFoundException("No available plate numbers for owner with National ID: " + nationalId);
        }
        
        return availablePlates.get(0);
    }
    
    @Override
    @Transactional
    public void markPlateAsInUse(PlateNumber plateNumber) {
        plateNumber.setStatus(PlateNumber.PlateStatus.IN_USE);
        plateNumberRepository.save(plateNumber);
        logger.info("Plate number marked as IN_USE: {}", plateNumber.getPlateNumber());
    }
    
    @Override
    @Transactional
    public void markPlateAsAvailable(PlateNumber plateNumber) {
        plateNumber.setStatus(PlateNumber.PlateStatus.AVAILABLE);
        plateNumberRepository.save(plateNumber);
        logger.info("Plate number marked as AVAILABLE: {}", plateNumber.getPlateNumber());
    }
    
    @Override
    public boolean plateNumberExists(String plateNumber) {
        return plateNumberRepository.existsByPlateNumber(plateNumber);
    }
    
    private PlateNumberResponse mapToPlateNumberResponse(PlateNumber plateNumber) {
        PlateNumberResponse response = new PlateNumberResponse();
        response.setId(plateNumber.getId());
        response.setPlateNumber(plateNumber.getPlateNumber());
        response.setIssuedDate(plateNumber.getIssuedDate());
        response.setStatus(plateNumber.getStatus().name());
        response.setOwnerName(plateNumber.getOwner().getFullName());
        response.setOwnerNationalId(plateNumber.getOwner().getNationalId());
        return response;
    }
}
