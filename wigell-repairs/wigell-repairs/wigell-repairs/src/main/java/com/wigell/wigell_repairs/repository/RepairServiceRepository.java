package com.wigell.wigell_repairs.repository;

import com.wigell.wigell_repairs.entity.RepairServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairServiceRepository extends JpaRepository<RepairServiceEntity, Long> {
}
