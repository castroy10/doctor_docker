package ru.castroy10.doctor.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.castroy10.doctor.model.Notifable;
import ru.castroy10.doctor.model.Visit;

import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(Notifable model, Visit visit) {
        String email = model.getEmail();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("Doctor System <castroy10@yandex.ru>");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Напоминание о визите");
        simpleMailMessage.setText("Создан визит пациента " + visit.getPatient().getFullName() + " " + visit.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        javaMailSender.send(simpleMailMessage);
    }
}
