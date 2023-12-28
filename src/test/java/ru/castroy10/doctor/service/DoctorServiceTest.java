package ru.castroy10.doctor.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.repository.DoctorRepository;
import ru.castroy10.doctor.repository.PatientRepository;

import java.util.List;

@SpringBootTest
class DoctorServiceTest {

    @MockBean
    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;
    private final Doctor doctor = new Doctor();

    @Autowired
    DoctorServiceTest(DoctorRepository doctorRepository, DoctorService doctorService) {
        this.doctorRepository = doctorRepository;
        this.doctorService = doctorService;
    }

    @BeforeEach
    public void init() {
        doctor.setId(1L);
        doctor.setLastName("Тестовый доктор");
    }

    @Test
    void findByName() {
        Mockito.when(doctorRepository.findByName(Mockito.any(String.class))).thenReturn(List.of(doctor));
        Assertions.assertEquals("Тестовый доктор", doctorService.findByName("Иванов").get(0).getLastName());
    }
}