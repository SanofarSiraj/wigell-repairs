package com.wigell.wigell_repairs.controller;

import com.wigell.wigell_repairs.Dto.BookingRequest;
import com.wigell.wigell_repairs.entity.Booking;
import com.wigell.wigell_repairs.entity.RepairServiceEntity;
import com.wigell.wigell_repairs.service.RepairService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wigellrepairs")
public class CustomerController {

    private final RepairService repairService;

    public CustomerController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping("/services")
    public List<RepairServiceEntity> listServices() {
        return repairService.listServices();
    }

    @PostMapping("/bookservice")
    public ResponseEntity<Booking> bookService(@RequestBody BookingRequest req, Authentication auth) {
        // trust the passed customerName or prefer authenticated username?
        if (req.getCustomerName() == null || req.getCustomerName().isBlank()) {
            req.setCustomerName(auth.getName());
        }
        Booking b = repairService.bookService(req);
        return ResponseEntity.ok(b);
    }

    @PutMapping("/cancelbooking")
    public ResponseEntity<?> cancelBooking(@RequestParam Long bookingId) {
        try {
            Booking b = repairService.cancelBooking(bookingId);
            return ResponseEntity.ok(b);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mybookings")
    public List<Booking> myBookings(@RequestParam String customerName) {
        return repairService.getBookingsByCustomer(customerName);
    }
}
