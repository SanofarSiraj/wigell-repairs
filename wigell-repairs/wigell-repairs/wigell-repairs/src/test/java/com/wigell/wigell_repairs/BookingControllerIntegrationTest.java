package com.wigell.wigell_repairs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wigell.wigell_repairs.Dto.AddServiceRequest;
import com.wigell.wigell_repairs.Dto.BookingRequest;
import com.wigell.wigell_repairs.entity.ServiceType;
import com.wigell.wigell_repairs.entity.Technician;
import com.wigell.wigell_repairs.repository.RepairServiceRepository;
import com.wigell.wigell_repairs.repository.TechnicianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// full context test with H2 (test profile)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private RepairServiceRepository repairServiceRepository;

    @Autowired
    private ObjectMapper mapper;

    private String userAuth = "user1:password";

    @BeforeEach
    void setUp() {
        technicianRepository.deleteAll();
        repairServiceRepository.deleteAll();

        Technician t = new Technician("IntegrationTech", "General");
        technicianRepository.save(t);

        AddServiceRequest req = new AddServiceRequest();
        req.setName("Integration Service");
        req.setType(ServiceType.OTHER);
        req.setPriceSek(BigDecimal.valueOf(1000));
        req.setTechnicianId(t.getId());

        // add via repository for simplicity
        var entity = new com.wigell.wigell_repairs.entity.RepairServiceEntity(req.getName(), req.getType(), req.getPriceSek(), t);
        repairServiceRepository.save(entity);
    }

    @Test
    void book_and_get_bookings_as_user() throws Exception {
        var services = repairServiceRepository.findAll();
        Long serviceId = services.get(0).getId();

        BookingRequest br = new BookingRequest();
        br.setCustomerName("user1");
        br.setServiceId(serviceId);
        br.setDate(LocalDate.now().plusDays(3));

        mockMvc.perform(post("/api/wigellrepairs/bookservice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(br))
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(userAuth.getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("user1"))
                .andExpect(jsonPath("$.service.name").value("Integration Service"));

        mockMvc.perform(get("/api/wigellrepairs/mybookings")
                        .param("customerName", "user1")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(userAuth.getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("user1"));
    }
}
