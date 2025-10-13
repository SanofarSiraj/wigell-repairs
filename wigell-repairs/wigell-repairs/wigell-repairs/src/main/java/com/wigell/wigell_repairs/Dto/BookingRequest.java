package com.wigell.wigell_repairs.Dto;

import java.time.LocalDate;

public class BookingRequest {
    private String customerName;
    private Long serviceId;
    private LocalDate date;

    public BookingRequest() {}

    public String getCustomerName() { return customerName; }
    public Long getServiceId() { return serviceId; }
    public LocalDate getDate() { return date; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public void setDate(LocalDate date) { this.date = date; }
}
