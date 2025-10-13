package com.wigell.wigell_repairs.service;

import com.wigell.wigell_repairs.Dto.AddServiceRequest;
import com.wigell.wigell_repairs.Dto.BookingRequest;
import com.wigell.wigell_repairs.entity.*;
import com.wigell.wigell_repairs.repository.BookingRepository;
import com.wigell.wigell_repairs.repository.RepairServiceRepository;
import com.wigell.wigell_repairs.repository.TechnicianRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RepairService {

    private static final Logger logger = LoggerFactory.getLogger(RepairService.class);

    private final RepairServiceRepository serviceRepo;
    private final TechnicianRepository technicianRepo;
    private final BookingRepository bookingRepo;
    private final CurrencyConversionService conversionService;

    public RepairService(RepairServiceRepository serviceRepo,
                         TechnicianRepository technicianRepo,
                         BookingRepository bookingRepo,
                         CurrencyConversionService conversionService) {
        this.serviceRepo = serviceRepo;
        this.technicianRepo = technicianRepo;
        this.bookingRepo = bookingRepo;
        this.conversionService = conversionService;
    }

    public List<RepairServiceEntity> listServices() {
        return serviceRepo.findAll();
    }

    public List<com.wigell.wigell_repairs.entity.Technician> listTechnicians() {
        return technicianRepo.findAll();
    }

    @Transactional
    public com.wigell.wigell_repairs.entity.Technician addTechnician(String name, String expertise) {
        com.wigell.wigell_repairs.entity.Technician t = new com.wigell.wigell_repairs.entity.Technician(name, expertise);
        com.wigell.wigell_repairs.entity.Technician saved = technicianRepo.save(t);
        logger.info("admin added technician {} (expertise={})", name, expertise);
        return saved;
    }

    @Transactional
    public RepairServiceEntity addService(AddServiceRequest req) {
        com.wigell.wigell_repairs.entity.Technician tech = technicianRepo.findById(req.getTechnicianId())
                .orElseThrow(() -> new IllegalArgumentException("Technician not found"));
        RepairServiceEntity s = new RepairServiceEntity(req.getName(), req.getType(), req.getPriceSek(), tech);
        RepairServiceEntity saved = serviceRepo.save(s);
        logger.info("admin added service {} type {} price {} SEK", saved.getName(), saved.getType(), saved.getPriceSek());
        return saved;
    }

    @Transactional
    public RepairServiceEntity updateService(Long id, AddServiceRequest req) {
        RepairServiceEntity existing = serviceRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Service not found"));
        existing.setName(req.getName());
        existing.setType(req.getType());
        existing.setPriceSek(req.getPriceSek());
        Technician tech = technicianRepo.findById(req.getTechnicianId()).orElseThrow(() -> new IllegalArgumentException("Technician not found"));
        existing.setTechnician(tech);
        RepairServiceEntity updated = serviceRepo.save(existing);
        logger.info("admin updated service id={} name={}", updated.getId(), updated.getName());
        return updated;
    }

    @Transactional
    public void removeService(Long id) {
        serviceRepo.deleteById(id);
        logger.info("admin removed service id={}", id);
    }

    @Transactional
    public Booking bookService(BookingRequest req) {
        RepairServiceEntity service = serviceRepo.findById(req.getServiceId()).orElseThrow(() -> new IllegalArgumentException("Service not found"));
        BigDecimal priceSek = service.getPriceSek();
        BigDecimal priceEur = conversionService.sekToEur(priceSek);
        Booking booking = new Booking(req.getCustomerName(), service, req.getDate(), priceSek, priceEur);
        Booking saved = bookingRepo.save(booking);
        logger.info("user {} booked repair '{}' with technician {} on {} ({} SEK / {} EUR)",
                req.getCustomerName(), service.getName(), service.getTechnician().getName(), req.getDate(), priceSek, priceEur);
        return saved;
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        LocalDate now = LocalDate.now();
        LocalDate lastAllowed = booking.getDate().minusDays(1); // up until 1 day before scheduled date allowed
        if (now.isAfter(lastAllowed)) {
            throw new IllegalStateException("Cannot cancel booking within 1 day of scheduled date");
        }
        booking.setCanceled(true);
        Booking saved = bookingRepo.save(booking);
        logger.info("user {} canceled booking id={} for service '{}' on {}",
                booking.getCustomerName(), saved.getId(), saved.getService().getName(), saved.getDate());
        return saved;
    }

    public List<Booking> getBookingsByCustomer(String customerName) {
        return bookingRepo.findByCustomerName(customerName);
    }

    public List<Booking> listCanceled() { return bookingRepo.findByCanceledTrue(); }

    public List<Booking> listUpcoming() { return bookingRepo.findByDateGreaterThanEqualAndCanceledFalse(LocalDate.now()); }

    public List<Booking> listPast() { return bookingRepo.findByDateBeforeAndCanceledFalse(LocalDate.now()); }
}
