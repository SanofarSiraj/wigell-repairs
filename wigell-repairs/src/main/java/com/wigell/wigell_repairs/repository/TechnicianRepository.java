package com.wigell.wigell_repairs.repository;

import com.wigell.wigell_repairs.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {
}
