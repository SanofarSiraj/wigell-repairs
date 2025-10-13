package com.wigell.wigell_repairs.repository;

import com.wigell.wigell_repairs.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerName(String customerName);
    List<Booking> findByCanceledTrue();
    List<Booking> findByDateAfterAndCanceledFalse(LocalDate date);
    List<Booking> findByDateBeforeAndCanceledFalse(LocalDate date);
    List<Booking> findByDateGreaterThanEqualAndCanceledFalse(LocalDate date);
}
