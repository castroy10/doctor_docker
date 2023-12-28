package ru.castroy10.doctor.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.model.Visit;
import ru.castroy10.doctor.service.DoctorService;
import ru.castroy10.doctor.service.ExcelService;
import ru.castroy10.doctor.service.PatientService;
import ru.castroy10.doctor.service.VisitService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("Create report")
@Route("createreport")
@PermitAll
public class CreateReportView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(CreateReportView.class);
    private final VisitService visitService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ExcelService excelService;
    private Patient patient = new Patient();
    private Doctor doctor = new Doctor();
    private List<Visit> visitList = new ArrayList<>();
    private final TextField filterPatient = new TextField();
    private final Grid<Patient> patientGrid = new Grid<>(Patient.class, false);
    private final TextField filterDoctor = new TextField();
    private final Grid<Doctor> doctorGrid = new Grid<>(Doctor.class, false);
    private final Grid<Visit> visitGrid = new Grid<>(Visit.class, false);
    private final DatePicker dateTimeAfter = new DatePicker();
    private final DatePicker dateTimeBefore = new DatePicker();
    private final Button buttonClearDate = new Button("Очистить даты");
    private final Button buttonSearch = new Button("Поиск");
    private final Button buttonSave = new Button("Сохранить в Excel");
    private final Button buttonBack = new Button("Назад");
    Div divText = new Div();
    private final VerticalLayout verticalLayout = new VerticalLayout(filterPatient, patientGrid, filterDoctor, doctorGrid, divText, visitGrid, dateTimeAfter, dateTimeBefore, buttonClearDate, buttonSearch, buttonBack, buttonSave);

    public CreateReportView(VisitService visitService, PatientService patientService, DoctorService doctorService, ExcelService excelService) {
        this.visitService = visitService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.excelService = excelService;
        createScene();

        filterPatient.addValueChangeListener(field -> {
            if (field.getValue().isEmpty()) {
                patientGrid.setVisible(false);
                patient.setId(null);
            }
            if (field.getValue().length() > 2) {
                List<Patient> list = this.patientService.findByName(field.getValue().toLowerCase());
                if (!list.isEmpty() && list.size() < 25) {
                    patientGrid.setPageSize(list.size());
                    patientGrid.setAllRowsVisible(true);
                    patientGrid.recalculateColumnWidths();
                } else {
                    patientGrid.setAllRowsVisible(false);
                }
                patientGrid.setItems(list);
                patientGrid.setVisible(true);
            }
        });

        filterDoctor.addValueChangeListener(field -> {
            if (field.getValue().isEmpty()) {
                doctorGrid.setVisible(false);
                doctor.setId(null);
            }
            if (field.getValue().length() > 2) {
                List<Doctor> list = this.doctorService.findByName(field.getValue().toLowerCase());
                if (!list.isEmpty() && list.size() < 25) {
                    doctorGrid.setPageSize(list.size());
                    doctorGrid.setAllRowsVisible(true);
                    doctorGrid.recalculateColumnWidths();
                } else {
                    doctorGrid.setAllRowsVisible(false);
                }
                doctorGrid.setItems(list);
                doctorGrid.setVisible(true);
            }
        });

        patientGrid.addItemClickListener(e -> {
            patient = e.getItem();
        });

        doctorGrid.addItemClickListener(e -> {
            doctor = e.getItem();
        });

        buttonClearDate.addClickListener(e -> {
            dateTimeAfter.clear();
            dateTimeBefore.clear();
        });

        buttonSearch.addClickListener(e -> {
            search();
        });

        buttonSave.addClickListener(e -> {
            save();
        });

        visitGrid.addItemClickListener(e -> {
            Visit visit = e.getItem();
            getUI().ifPresent(ui -> ui.navigate(VisitView.class, visit.getId()));
        });

        buttonBack.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(MainView.class));
        });
    }

    private void createScene() {
        setAlignItems(Alignment.CENTER);

        add(new H1("Создание отчета по визитам"));
        add(verticalLayout);
        patientGrid.setVisible(false);
        doctorGrid.setVisible(false);
        visitGrid.setVisible(false);

        filterPatient.setPlaceholder("Поиск пациента. Минимум 3 символа");
        filterPatient.setWidth("350px");
        filterPatient.setValueChangeMode(ValueChangeMode.EAGER);

        filterDoctor.setPlaceholder("Поиск доктора. Минимум 3 символа");
        filterDoctor.setWidth("350px");
        filterDoctor.setValueChangeMode(ValueChangeMode.EAGER);

        patientGrid.addColumn(patient -> patient.getLastName()).setHeader("Фамилия").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getFirstName()).setHeader("Имя").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getMiddleName()).setHeader("Отчество").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getDate()).setHeader("Дата рождения").setAutoWidth(true).setSortable(true);

        doctorGrid.addColumn(d -> d.getLastName()).setHeader("Фамилия").setAutoWidth(true).setSortable(true);
        doctorGrid.addColumn(d -> d.getFirstName()).setHeader("Имя").setAutoWidth(true).setSortable(true);
        doctorGrid.addColumn(d -> d.getMiddleName()).setHeader("Отчество").setAutoWidth(true).setSortable(true);
        doctorGrid.addColumn(d -> d.getCategory().stream().findAny().get().getCategoryName().toString()).setHeader("Специализация").setAutoWidth(true).setSortable(true);

        divText.getElement().getStyle().set("font-size", "40px");
        divText.getElement().getStyle().set("margin", "auto");
        divText.setVisible(false);
        divText.setText("Результаты поиска:");

        visitGrid.addColumn(v -> v.getPatient().getFullName()).setHeader("Пациент").setAutoWidth(true).setSortable(true);
        visitGrid.addColumn(v -> v.getDoctor().getFullName()).setHeader("Доктор").setAutoWidth(true).setSortable(true);
        visitGrid.addColumn(v -> v.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).setHeader("Дата и время").setAutoWidth(true).setSortable(true);
        visitGrid.addColumn(v -> v.isFinished()).setHeader("Завершен").setAutoWidth(true).setSortable(true).setRenderer(new ComponentRenderer<>(v -> v.isFinished() ? new Icon(VaadinIcon.CHECK) : null));
    }

    private void search() {
        if (isValidSearchParameters()) {
            visitList = getVisitListBySearchParameters();
            if (!visitList.isEmpty() && visitList.size() < 25) {
                visitGrid.setPageSize(visitList.size());
                visitGrid.setAllRowsVisible(true);
                visitGrid.recalculateColumnWidths();
            } else {
                visitGrid.setAllRowsVisible(false);
            }
            visitGrid.setItems(visitList);
        } else {
            Notification.show("Недостаточно параметров для поиска");
        }
    }

    private boolean isValidSearchParameters() {
        return patient.getId() != null || doctor.getId() != null || dateTimeAfter.getValue() != null || dateTimeBefore.getValue() != null;
    }

    private List<Visit> getVisitListBySearchParameters() {
        visitGrid.setVisible(true);
        divText.setVisible(true);
        if (patient.getId() != null && doctor.getId() != null && dateTimeAfter.getValue() != null && dateTimeBefore.getValue() != null) {
            return visitService.findAllVisitForPatient(patient, doctor, dateTimeAfter.getValue().atStartOfDay(), dateTimeBefore.getValue().atStartOfDay());
        } else if (doctor.getId() != null && dateTimeAfter.getValue() != null && dateTimeBefore.getValue() != null) {
            return visitService.findAllVisitForPatient(doctor, dateTimeAfter.getValue().atStartOfDay(), dateTimeBefore.getValue().atStartOfDay());
        } else if (patient.getId() != null && dateTimeAfter.getValue() != null && dateTimeBefore.getValue() != null) {
            return visitService.findAllVisitForPatient(patient, dateTimeAfter.getValue().atStartOfDay(), dateTimeBefore.getValue().atStartOfDay());
        } else if (patient.getId() != null && doctor.getId() != null && dateTimeAfter.getValue() == null && dateTimeBefore.getValue() == null) {
            return visitService.findAllVisitForPatient(patient, doctor);
        } else if (patient.getId() != null && doctor.getId() == null && dateTimeAfter.getValue() == null && dateTimeBefore.getValue() == null) {
            return visitService.findAllVisitForPatient(patient);
        } else if (doctor.getId() != null && patient.getId() == null && dateTimeAfter.getValue() == null && dateTimeBefore.getValue() == null) {
            return visitService.findAllVisitForPatient(doctor);
        } else {
            Notification.show("Недостаточно параметров для поиска");
            visitGrid.setVisible(false);
            divText.setVisible(false);
            return Collections.emptyList();
        }
    }

    private void save() {
        String pathFile = excelService.save(visitList);
        byte[] array = new byte[0];
        try {
            array = Files.readAllBytes(Paths.get(pathFile));
        } catch (IOException e) {
            log.error("Ошибка чтения файла {}, {}", pathFile, e.getMessage());
        }
        byte[] finalArray = array;
        StreamResource streamResource = new StreamResource("visits_save.xlsx", (InputStreamFactory) () -> new ByteArrayInputStream(finalArray));
        Anchor downloadLink = new Anchor(streamResource, "");
        downloadLink.getElement().getThemeList().add("button");
        downloadLink.getStyle().set("display", "none");
        add(downloadLink);
        downloadLink.getElement().executeJs("this.click()");
    }
}