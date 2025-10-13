package com.wigell.wigell_repairs.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "repair_services")
public class RepairServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ServiceType type;

    private java.math.BigDecimal priceSek;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private com.wigell.wigell_repairs.entity.Technician technician;

    public RepairServiceEntity() {}

    public RepairServiceEntity(String name, ServiceType type, java.math.BigDecimal priceSek, com.wigell.wigell_repairs.entity.Technician technician) {
        this.name = name;
        this.type = type;
        this.priceSek = priceSek;
        this.technician = technician;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public ServiceType getType() { return type; }
    public java.math.BigDecimal getPriceSek() { return priceSek; }
    public com.wigell.wigell_repairs.entity.Technician getTechnician() { return technician; }

    public void setName(String name) { this.name = name; }
    public void setType(ServiceType type) { this.type = type; }
    public void setPriceSek(java.math.BigDecimal priceSek) { this.priceSek = priceSek; }
    public void setTechnician(com.wigell.wigell_repairs.entity.Technician technician) { this.technician = technician; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepairServiceEntity)) return false;
        RepairServiceEntity that = (RepairServiceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
