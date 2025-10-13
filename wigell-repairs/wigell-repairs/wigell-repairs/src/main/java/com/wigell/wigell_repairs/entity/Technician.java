package com.wigell.wigell_repairs.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "technicians")
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String expertise;

    public Technician() {}

    public Technician(String name, String expertise) {
        this.name = name;
        this.expertise = expertise;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getExpertise() { return expertise; }
    public void setName(String name) { this.name = name; }
    public void setExpertise(String expertise) { this.expertise = expertise; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Technician t = (Technician) o;
        return Objects.equals(id, t.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
