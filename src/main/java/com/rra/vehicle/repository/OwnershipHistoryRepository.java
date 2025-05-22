
package com.rra.vehicle.repository;

import com.rra.vehicle.model.OwnershipHistory;
import com.rra.vehicle.model.Vehicle;
import com.rra.vehicle.model.VehicleOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnershipHistoryRepository extends JpaRepository<OwnershipHistory, Long> {
    List<OwnershipHistory> findByVehicleOrderByStartDateDesc(Vehicle vehicle);
    List<OwnershipHistory> findByOwnerOrderByStartDateDesc(VehicleOwner owner);
    Page<OwnershipHistory> findByVehicle(Vehicle vehicle, Pageable pageable);
}
