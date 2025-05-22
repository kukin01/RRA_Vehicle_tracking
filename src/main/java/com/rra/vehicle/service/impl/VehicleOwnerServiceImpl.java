
package com.rra.vehicle.service.impl;

import com.rra.vehicle.dto.owner.OwnerRequest;
import com.rra.vehicle.dto.owner.OwnerResponse;
import com.rra.vehicle.exception.ResourceAlreadyExistsException;
import com.rra.vehicle.exception.ResourceNotFoundException;
import com.rra.vehicle.model.VehicleOwner;
import com.rra.vehicle.repository.VehicleOwnerRepository;
import com.rra.vehicle.service.VehicleOwnerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleOwnerServiceImpl implements VehicleOwnerService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleOwnerServiceImpl.class);
    
    @Autowired
    private VehicleOwnerRepository ownerRepository;

    @Override
    @Transactional
    public OwnerResponse registerOwner(OwnerRequest ownerRequest) {
        if (ownerRepository.existsByNationalId(ownerRequest.getNationalId())) {
            throw new ResourceAlreadyExistsException("Owner with this National ID already exists");
        }

        VehicleOwner owner = new VehicleOwner();
        owner.setFullName(ownerRequest.getFullName());
        owner.setNationalId(ownerRequest.getNationalId());
        owner.setPhoneNumber(ownerRequest.getPhoneNumber());
        owner.setAddress(ownerRequest.getAddress());

        VehicleOwner savedOwner = ownerRepository.save(owner);
        logger.info("Vehicle owner registered: {}", savedOwner.getNationalId());

        return mapToOwnerResponse(savedOwner);
    }

    @Override
    public Page<OwnerResponse> getAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable).map(this::mapToOwnerResponse);
    }

    @Override
    public OwnerResponse getOwnerById(Long id) {
        VehicleOwner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + id));
        return mapToOwnerResponse(owner);
    }

    @Override
    public OwnerResponse getOwnerByNationalId(String nationalId) {
        VehicleOwner owner = ownerRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with National ID: " + nationalId));
        return mapToOwnerResponse(owner);
    }
    
    @Override
    public VehicleOwner getOwnerEntityByNationalId(String nationalId) {
        return ownerRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with National ID: " + nationalId));
    }
    
    @Override
    public boolean ownerExists(String nationalId) {
        return ownerRepository.existsByNationalId(nationalId);
    }

    private OwnerResponse mapToOwnerResponse(VehicleOwner owner) {
        OwnerResponse response = new OwnerResponse();
        response.setId(owner.getId());
        response.setFullName(owner.getFullName());
        response.setNationalId(owner.getNationalId());
        response.setPhoneNumber(owner.getPhoneNumber());
        response.setAddress(owner.getAddress());
        response.setCreatedAt(owner.getCreatedAt());
        return response;
    }
}
