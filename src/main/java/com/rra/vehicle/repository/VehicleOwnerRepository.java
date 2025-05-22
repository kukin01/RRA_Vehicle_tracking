
package com.rra.vehicle.repository;

import com.rra.vehicle.model.VehicleOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleOwnerRepository extends JpaRepository<VehicleOwner, Long> {
    Optional<VehicleOwner> findByNationalId(String nationalId);
    Optional<VehicleOwner> findByPhoneNumber(String phoneNumber);
    Page<VehicleOwner> findAll(Pageable pageable);
    boolean existsByNationalId(String nationalId);
}
