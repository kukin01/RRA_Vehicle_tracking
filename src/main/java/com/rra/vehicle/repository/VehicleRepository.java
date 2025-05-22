
package com.rra.vehicle.repository;

import com.rra.vehicle.model.PlateNumber;
import com.rra.vehicle.model.Vehicle;
import com.rra.vehicle.model.VehicleOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByChassisNumber(String chassisNumber);
    Optional<Vehicle> findByCurrentPlate(PlateNumber plateNumber);
    Page<Vehicle> findByCurrentOwner(VehicleOwner owner, Pageable pageable);
    boolean existsByChassisNumber(String chassisNumber);
}
