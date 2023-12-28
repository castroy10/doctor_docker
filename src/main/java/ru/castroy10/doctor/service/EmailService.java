package ru.castroy10.doctor.service;

import ru.castroy10.doctor.model.Notifable;
import ru.castroy10.doctor.model.Visit;

import java.time.LocalDateTime;

public interface EmailService {
    void send(Notifable model, Visit visit);
}
