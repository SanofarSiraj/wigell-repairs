package com.wigell.wigell_repairs.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName; // <-- keep this for unit tests

    @ManyToOne
    @JoinColumn(name = "service_id")
    private RepairServiceEntity service;

    private LocalDate date;
    private BigDecimal totalPriceSek;
    private BigDecimal totalPriceEur;
    private boolean canceled;
    private LocalDateTime createdAt;

    public Booking() {}

    public Booking(String customerName, RepairServiceEntity service, LocalDate date,
                   BigDecimal totalPriceSek, BigDecimal totalPriceEur) {
        this.customerName = customerName;
        this.service = service;
        this.date = date;
        this.totalPriceSek = totalPriceSek;
        this.totalPriceEur = totalPriceEur;
        this.canceled = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public RepairServiceEntity getService() { return service; }
    public LocalDate getDate() { return date; }
    public BigDecimal getTotalPriceSek() { return totalPriceSek; }
    public BigDecimal getTotalPriceEur() { return totalPriceEur; }
    public boolean isCanceled() { return canceled; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCanceled(boolean canceled) { this.canceled = canceled; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking b = (Booking) o;
        return Objects.equals(id, b.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
