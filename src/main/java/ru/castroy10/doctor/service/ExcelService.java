package ru.castroy10.doctor.service;

import ru.castroy10.doctor.model.Visit;

import java.util.List;

public interface ExcelService {
    String save(List<Visit> visitList);
}
