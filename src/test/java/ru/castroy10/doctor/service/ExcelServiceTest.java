package ru.castroy10.doctor.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.model.Visit;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class ExcelServiceTest {

    @MockBean
    private final ExcelService excelService;

    @Autowired
    ExcelServiceTest(ExcelService excelService) {
        this.excelService = excelService;
    }

    @Test
    void saveFile() {
        Visit visit = new Visit();
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        LocalDateTime localDateTime = LocalDateTime.now();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setDateTime(localDateTime);
        List<Visit> visitList = List.of(visit);
        excelService.save(visitList);
        Mockito.verify(excelService, Mockito.times(1)).save(visitList);
    }
}