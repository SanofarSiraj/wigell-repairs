package com.wigell.wigell_repairs;

import com.wigell.wigell_repairs.Dto.AddServiceRequest;
import com.wigell.wigell_repairs.Dto.BookingRequest;
import com.wigell.wigell_repairs.entity.Booking;
import com.wigell.wigell_repairs.entity.RepairServiceEntity;
import com.wigell.wigell_repairs.entity.ServiceType;
import com.wigell.wigell_repairs.entity.Technician;
import com.wigell.wigell_repairs.repository.BookingRepository;
import com.wigell.wigell_repairs.repository.RepairServiceRepository;
import com.wigell.wigell_repairs.repository.TechnicianRepository;
import com.wigell.wigell_repairs.service.CurrencyConversionService;
import com.wigell.wigell_repairs.service.RepairService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepairServiceUnitTest {

    private RepairServiceRepository serviceRepo;
    private TechnicianRepository technicianRepo;
    private BookingRepository bookingRepo;
    private CurrencyConversionService conversionService;
    private RepairService service;

    @BeforeEach
    void setUp() {
        serviceRepo = mock(RepairServiceRepository.class);
        technicianRepo = mock(TechnicianRepository.class);
        bookingRepo = mock(BookingRepository.class);
        conversionService = new CurrencyConversionService();
        service = new RepairService(serviceRepo, technicianRepo, bookingRepo, conversionService);
    }

    @Test
    void addServiceAndUpdate_andRemove_flow() {
        Technician tech = new Technician("Tomas", "Cars");
        tech.setExpertise("Cars");
        when(technicianRepo.findById(1L)).thenReturn(Optional.of(tech));
        AddServiceRequest req = new AddServiceRequest();
        req.setName("Oil change");
        req.setType(ServiceType.CAR);
        req.setPriceSek(BigDecimal.valueOf(500));
        req.setTechnicianId(1L);

        RepairServiceEntity saved = new RepairServiceEntity("Oil change", ServiceType.CAR, BigDecimal.valueOf(500), tech);
        when(serviceRepo.save(any())).thenReturn(saved);

        RepairServiceEntity added = service.addService(req);
        assertNotNull(added);
        assertEquals("Oil change", added.getName());

        // update path: serviceRepo.findById should return existing
        RepairServiceEntity existing = new RepairServiceEntity("Old", ServiceType.CAR, BigDecimal.valueOf(400), tech);
        when(serviceRepo.findById(2L)).thenReturn(Optional.of(existing));
        when(technicianRepo.findById(1L)).thenReturn(Optional.of(tech));
        when(serviceRepo.save(existing)).thenReturn(existing);

        AddServiceRequest updateReq = new AddServiceRequest();
        updateReq.setName("Updated");
        updateReq.setPriceSek(BigDecimal.valueOf(600));
        updateReq.setType(ServiceType.CAR);
        updateReq.setTechnicianId(1L);

        RepairServiceEntity updated = service.updateService(2L, updateReq);
        assertEquals("Updated", updated.getName());

        // remove
        doNothing().when(serviceRepo).deleteById(3L);
        service.removeService(3L);
        verify(serviceRepo, times(1)).deleteById(3L);
    }

    @Test
    void book_and_cancel_workflow() {
        Technician tech = new Technician("Anna", "Electronics");
        RepairServiceEntity rs = new RepairServiceEntity("Phone fix", ServiceType.ELECTRONICS, BigDecimal.valueOf(800), tech);
        when(serviceRepo.findById(10L)).thenReturn(Optional.of(rs));

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        when(bookingRepo.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BookingRequest br = new BookingRequest();
        br.setCustomerName("user1");
        br.setServiceId(10L);
        br.setDate(LocalDate.now().plusDays(5));

        Booking booked = service.bookService(br);
        assertNotNull(booked);
        assertEquals("user1", booked.getCustomerName());
        assertEquals(BigDecimal.valueOf(800), booked.getTotalPriceSek());
        assertTrue(booked.getTotalPriceEur().compareTo(BigDecimal.ZERO) > 0);

        // cancel allowed: bookingRepo.findById returns booking with date in 5 days
        Booking persisted = new Booking("user1", rs, LocalDate.now().plusDays(5), BigDecimal.valueOf(800), conversionService.sekToEur(BigDecimal.valueOf(800)));
        when(bookingRepo.findById(99L)).thenReturn(Optional.of(persisted));
        when(bookingRepo.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Booking canceled = service.cancelBooking(99L);
        assertTrue(canceled.isCanceled());

        // cancel not allowed branch: date = today (within 1 day)
        Booking late = new Booking("user1", rs, LocalDate.now(), BigDecimal.valueOf(800), conversionService.sekToEur(BigDecimal.valueOf(800)));
        when(bookingRepo.findById(100L)).thenReturn(Optional.of(late));
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.cancelBooking(100L));
        assertEquals("Cannot cancel booking within 1 day of scheduled date", ex.getMessage());
    }
}
