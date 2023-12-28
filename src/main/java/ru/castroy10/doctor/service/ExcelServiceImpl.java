package ru.castroy10.doctor.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.castroy10.doctor.model.Visit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

    @Override
    public String save(List<Visit> visitList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Visits");
        Row header = sheet.createRow(0);

        createHeader(header, workbook);
        createBody(visitList, sheet);
        return saveFile(workbook);
    }

    private String saveFile(Workbook workbook) {
        String tempFilePath = "";
        try {
            File tempFile = File.createTempFile("visits_save" + "#", ".tmp");
            tempFilePath = tempFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("Ошибка создания файла {}", e.getMessage());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFilePath)) {
            workbook.write(fileOutputStream);
            log.info("Файл успешно сохранен {} ", tempFilePath);
        } catch (IOException e) {
            log.error("Ошибка сохранения файла {}, {}", tempFilePath, e.getMessage());
        }
        return tempFilePath;
    }

    private void createHeader(Row header, Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("№");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Patient Full name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Doctor Full name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Date");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Diagnosis");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Treatment");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(6);
        headerCell.setCellValue("Comment");
        headerCell.setCellStyle(headerStyle);
    }

    private void createBody(List<Visit> visitList, Sheet sheet) {
        int rowNum = 1;
        for (Visit visit : visitList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1);
            row.createCell(1).setCellValue(visit.getPatient().getFullName());
            row.createCell(2).setCellValue(visit.getDoctor().getFullName());
            row.createCell(3).setCellValue(visit.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            row.createCell(4).setCellValue(visit.getDiagnosis());
            row.createCell(5).setCellValue(visit.getTreatment());
            row.createCell(6).setCellValue(visit.getComment());
        }
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
