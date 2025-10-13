package com.wigell.wigell_repairs.controller;

import com.wigell.wigell_repairs.Dto.AddServiceRequest;
import com.wigell.wigell_repairs.entity.Booking;
import com.wigell.wigell_repairs.entity.Technician;
import com.wigell.wigell_repairs.service.RepairService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wigellrepairs/admin")
public class AdminController {

    private final RepairService repairService;

    public AdminController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping("/listcanceled")
    public List<Booking> listCanceled() { return repairService.listCanceled(); }

    @GetMapping("/listupcoming")
    public List<Booking> listUpcoming() { return repairService.listUpcoming(); }

    @GetMapping("/listpast")
    public List<Booking> listPast() { return repairService.listPast(); }

    @PostMapping("/addservice")
    public ResponseEntity<?> addService(@RequestBody AddServiceRequest req) {
        try {
            return ResponseEntity.ok(repairService.addService(req));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateservice/{id}")
    public ResponseEntity<?> updateService(@PathVariable Long id, @RequestBody AddServiceRequest req) {
        try {
            return ResponseEntity.ok(repairService.updateService(id, req));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remservice/{id}")
    public ResponseEntity<?> removeService(@PathVariable Long id) {
        repairService.removeService(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addtechnician")
    public ResponseEntity<Technician> addTechnician(@RequestParam String name, @RequestParam String expertise) {
        return ResponseEntity.ok(repairService.addTechnician(name, expertise));
    }

    @GetMapping("/technicians")
    public List<Technician> listTechnicians() {
        return repairService.listTechnicians();
    }
}
