package ru.castroy10.doctor.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.model.Visit;
import ru.castroy10.doctor.repository.VisitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class VisitServiceTest {

    @MockBean
    private final VisitRepository visitRepository;

    private final VisitService visitService;
    private final Visit visit = new Visit();
    private final Patient patient = new Patient();
    private final Doctor doctor = new Doctor();

    @Autowired
    VisitServiceTest(VisitRepository visitRepository, VisitService visitService) {
        this.visitRepository = visitRepository;
        this.visitService = visitService;
    }

    @BeforeEach
    public void init() {
        doctor.setId(2L);
        patient.setId(1L);
        patient.setFirstName("Тестовый пациент");
        visit.setId(1L);
        visit.setPatient(patient);
        visit.setDoctor(doctor);
    }

    @Test
    public void testSave() {
        Mockito.when(visitRepository.save(Mockito.any(Visit.class))).thenReturn(visit);
        visitService.save(visit);
        Mockito.verify(visitRepository, Mockito.times(1)).save(visit);
    }

    @Test
    public void testSaveWithException() {
        Mockito.when(visitRepository.save(Mockito.any(Visit.class))).thenThrow(RuntimeException.class);
        Assertions.assertEquals(false, visitService.save(visit));
    }

    @Test
    public void testSaveWithNull() {
        Mockito.when(visitRepository.save(null)).thenReturn(visit);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            visitService.save(null);
        });
        Assertions.assertEquals("Visit must not be null", exception.getMessage());
    }

    @Test
    public void findVisitIsNotFinished() {
        List<Visit> visitList = List.of(visit);
        Mockito.when(visitRepository.findByIsFinishedFalse(Mockito.any(Long.class))).thenReturn(visitList);
        Assertions.assertEquals("Тестовый пациент", visitService.findVisitIsNotFinishedForDoctor(doctor).get(0).getPatient().getFirstName());
    }

    @Test
    public void findVisitForPatient() {
        List<Visit> visitList = List.of(visit);
        Mockito.when(visitRepository.findAllForPatient(Mockito.any(Long.class))).thenReturn(visitList);
        Assertions.assertEquals("Тестовый пациент", visitService.findAllVisitForPatient(patient).get(0).getPatient().getFirstName());
    }

    @Test
    public void findById() {
        Mockito.when(visitRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(visit));
        Assertions.assertEquals("Тестовый пациент", visitService.findById(1L).getPatient().getFirstName());
    }

    @Test
    public void findByName() {
        List<Visit> visitList = List.of(visit);
        Mockito.when(visitRepository.findByName(Mockito.any(String.class))).thenReturn(visitList);
        Assertions.assertEquals("Тестовый пациент", visitService.findByName("ина").get(0).getPatient().getFirstName());
    }

    @Test
    public void findAllVisitForPatient() {
        List<Visit> visitList = List.of(visit);
        LocalDateTime dateAfter = LocalDateTime.of(2017, 1, 1, 0, 0, 0);
        LocalDateTime dateBefore = LocalDateTime.of(2024, 1, 1, 0, 0);
        Mockito.when(visitRepository.findAllForPatientAndDoctorAndDate(Mockito.any(Long.class), Mockito.any(Long.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(visitList);
        Assertions.assertEquals("Тестовый пациент", visitService.findAllVisitForPatient(patient, doctor, dateAfter, dateBefore).get(0).getPatient().getFirstName());
    }
}