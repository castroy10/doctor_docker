package ru.castroy10.doctor.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.repository.PatientRepository;

import java.util.List;

@SpringBootTest
class PatientServiceTest {

    @MockBean
    private final PatientRepository patientRepository;

    private final PatientService patientService;
    private final Patient patient = new Patient();

    @Autowired
    PatientServiceTest(PatientRepository patientRepository, PatientService patientService) {
        this.patientRepository = patientRepository;
        this.patientService = patientService;
    }

    @BeforeEach
    public void init() {
        patient.setId(1L);
        patient.setLastName("Тестовый пациент");
    }

    @Test
    void findByName() {
        Mockito.when(patientRepository.findByName(Mockito.any(String.class))).thenReturn(List.of(patient));
        Assertions.assertEquals("Тестовый пациент", patientService.findByName("Иван").get(0).getLastName());
    }

    @Test
    void save() {
        Mockito.when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);
        patientService.save(patient);
        Mockito.verify(patientRepository,Mockito.times(1)).save(patient);
        Assertions.assertTrue(patientService.save(patient));
    }

    @Test
    void saveWithException() {
        Mockito.when(patientRepository.save(Mockito.any(Patient.class))).thenThrow(RuntimeException.class);
        Assertions.assertEquals(false, patientService.save(patient));
    }

}