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

    // ✅ List all services
    @GetMapping("/services")
    public List<RepairServiceEntity> listServices() {
        return repairService.listServices();
    }

    // ✅ Book a repair service
    @PostMapping("/bookservice")
    public ResponseEntity<Booking> bookService(@RequestBody BookingRequest req, Authentication auth) {
        // Always use the authenticated username, not the passed one
        String authenticatedUser = auth.getName();
        req.setCustomerName(authenticatedUser);
        Booking b = repairService.bookService(req);
        return ResponseEntity.ok(b);
    }

    // ✅ Cancel booking (only if it belongs to the logged-in user)
    @PutMapping("/cancelbooking")
    public ResponseEntity<?> cancelBooking(@RequestParam Long bookingId, Authentication auth) {
        String authenticatedUser = auth.getName();
        try {
            Booking booking = repairService.getBookingById(bookingId);

            if (!booking.getCustomerName().equals(authenticatedUser)) {
                return ResponseEntity.status(403)
                        .body("You can only cancel your own bookings.");
            }

            Booking canceled = repairService.cancelBooking(bookingId);
            return ResponseEntity.ok(canceled);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Booking not found.");
        }
    }

    // ✅ View my bookings (use logged-in user)
    @GetMapping("/mybookings")
    public List<Booking> myBookings(Authentication auth) {
        String authenticatedUser = auth.getName();
        return repairService.getBookingsByCustomer(authenticatedUser);
    }
}

