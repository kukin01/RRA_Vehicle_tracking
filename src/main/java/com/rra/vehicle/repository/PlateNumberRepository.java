
package com.rra.vehicle.repository;

import com.rra.vehicle.model.PlateNumber;
import com.rra.vehicle.model.VehicleOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlateNumberRepository extends JpaRepository<PlateNumber, Long> {
    Optional<PlateNumber> findByPlateNumber(String plateNumber);
    List<PlateNumber> findByOwnerAndStatus(VehicleOwner owner, PlateNumber.PlateStatus status);
    Page<PlateNumber> findByOwner(VehicleOwner owner, Pageable pageable);
    boolean existsByPlateNumber(String plateNumber);
}
