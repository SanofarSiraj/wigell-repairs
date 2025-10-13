package com.wigell.wigell_repairs.Dto;

import com.wigell.wigell_repairs.entity.ServiceType;

import java.math.BigDecimal;

public class AddServiceRequest {
    private String name;
    private ServiceType type;
    private BigDecimal priceSek;
    private Long technicianId;

    public AddServiceRequest() {}

    public String getName() { return name; }
    public ServiceType getType() { return type; }
    public BigDecimal getPriceSek() { return priceSek; }
    public Long getTechnicianId() { return technicianId; }

    public void setName(String name) { this.name = name; }
    public void setType(ServiceType type) { this.type = type; }
    public void setPriceSek(BigDecimal priceSek) { this.priceSek = priceSek; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }
}
