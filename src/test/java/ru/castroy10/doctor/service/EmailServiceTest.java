package ru.castroy10.doctor.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.model.Visit;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class EmailServiceTest {

    @MockBean
    private JavaMailSender javaMailSender;

    private final EmailService emailService;

    @Autowired
    EmailServiceTest(EmailService emailService) {
        this.emailService = emailService;
    }

    @Test
    void send() {
        Doctor model = new Doctor();
        Patient patient = new Patient();
        Visit visit = new Visit();
        model.setEmail("test@yandex.ru");
        LocalDateTime localDateTime = LocalDateTime.now();
        patient.setLastName("Тестовый");
        patient.setFirstName("Пациент");
        patient.setMiddleName("Иванович");
        visit.setPatient(patient);
        visit.setDateTime(localDateTime);
        emailService.send(model, visit);
        Mockito.verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}