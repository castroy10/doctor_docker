package ru.castroy10.doctor.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.castroy10.doctor.enumer.RoleName;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.model.Visit;
import ru.castroy10.doctor.service.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Create visit")
@Route("createvisit")
@PermitAll
public class CreateVisitView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(CreateVisitView.class);
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppUserService appUserService;
    private final VisitService visitService;
    private final EmailService emailService;
    private Visit visit = new Visit();
    Div divTextPatient = new Div();
    Div divTextDoctor = new Div();
    Div divTextDateTime = new Div();
    private final TextField filter = new TextField();
    private final Grid<Patient> patientGrid = new Grid<>(Patient.class, false);
    private final DateTimePicker dateTimePicker = new DateTimePicker();
    private final Button buttonPatientAdd = new Button("Добавить");
    private final Button buttonPatientCancel = new Button("Отменить пациента");
    private final Button buttonDoctorAdd = new Button("Добавить");
    private final Button buttonDoctorCancel = new Button("Отменить доктора");
    private final Button buttonDateTimeAdd = new Button("Добавить");
    private final Button buttonDateTimeCancel = new Button("Отменить время");
    private final Button buttonFinal = new Button("Создать визит");
    private final Button buttonBack = new Button("Назад");
    private final Select<Doctor> doctorSelect = new Select<>();
    private final VerticalLayout verticalLayout = new VerticalLayout(divTextPatient, divTextDoctor, divTextDateTime, filter, patientGrid, buttonPatientAdd, buttonPatientCancel, doctorSelect, buttonDoctorAdd, buttonDoctorCancel, dateTimePicker, buttonDateTimeAdd, buttonDateTimeCancel, buttonFinal, buttonBack);

    public CreateVisitView(PatientService patientService, DoctorService doctorService, AppUserService appUserService, VisitService visitService, EmailService emailService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appUserService = appUserService;
        this.visitService = visitService;
        this.emailService = emailService;
        createScene();

        filter.addValueChangeListener(field -> {
            if (field.getValue().length() > 2) {
                List<Patient> list = patientService.findByName(field.getValue().toLowerCase());
                if (list.size() > 0 && list.size() < 25) {
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

        patientGrid.addItemClickListener(e -> {
            visit.setPatient(e.getItem());
        });

        buttonPatientAdd.addClickListener(e -> {
            if (visit.getPatient().getId() != null) {
                divTextPatient.add(new Text("Пациент: " + visit.getPatient().getFullName()));
                buttonPatientAdd.setVisible(false);
                patientGrid.setVisible(false);
                filter.setVisible(false);
                filter.setValue("");
            }
        });

        buttonPatientCancel.addClickListener(e -> {
            visit.setPatient(null);
            divTextPatient.removeAll();
            buttonPatientAdd.setVisible(true);
            filter.setVisible(true);
        });

        buttonDoctorAdd.addClickListener(e -> {
            if (doctorSelect.getValue().getId() != null) {
                visit.setDoctor(doctorSelect.getValue());
                divTextDoctor.add(new Text("Доктор: " + doctorSelect.getValue().getFullName() + ", Специализация: "));
                divTextDoctor.add(new Text(doctorSelect.getValue().getCategory().stream().findFirst().get().getCategoryName().toString()));
                buttonDoctorAdd.setVisible(false);
                doctorSelect.setVisible(false);
            }
        });

        buttonDoctorCancel.addClickListener(e -> {
            doctorSelect.setValue(new Doctor());
            visit.setDoctor(null);
            divTextDoctor.removeAll();
            doctorSelect.setVisible(true);
            buttonDoctorAdd.setVisible(true);
        });

        buttonDateTimeAdd.addClickListener(e -> {
            if (dateTimePicker.getValue() != null) {
                visit.setDateTime(dateTimePicker.getValue());
                divTextDateTime.add("Время визита: " + dateTimePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                buttonDateTimeAdd.setVisible(false);
                dateTimePicker.setVisible(false);
            }
        });

        buttonDateTimeCancel.addClickListener(e -> {
            dateTimePicker.clear();
            visit.setDateTime(null);
            divTextDateTime.removeAll();
            buttonDateTimeAdd.setVisible(true);
            dateTimePicker.setVisible(true);
        });

        buttonFinal.addClickListener(e -> {
            if (visit.getPatient() != null && visit.getDoctor() != null && visit.getDateTime() != null && visitService.save(visit)) {
                emailService.send(visit.getDoctor(), visit);
                Notification.show("Визит успешно создан");
                log.info("Визит успешно создан id: " + visit.getId() + " пациент " + visit.getPatient().getFullName() + " доктор " + visit.getDoctor().getFullName());
                visit.setId(null);
            } else {
                Notification.show("Не удалось создать визит");
            }
        });

        buttonBack.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(MainView.class));
        });
    }

    private void createScene() {
        setAlignItems(Alignment.CENTER);
        patientGrid.setAllRowsVisible(true);

        filter.setPlaceholder("Поиск пациента. Минимум 3 символа");
        filter.setWidth("350px");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

        patientGrid.setVisible(false);
        patientGrid.addColumn(patient -> patient.getId()).setHeader("id").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getLastName()).setHeader("Фамилия").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getFirstName()).setHeader("Имя").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getMiddleName()).setHeader("Отчество").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getMobilePhone()).setHeader("Телефон").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getEmail()).setHeader("Почта").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getDate()).setHeader("Дата рождения").setAutoWidth(true).setSortable(true);

        doctorSelect.setLabel("Выберите доктора");
        doctorSelect.setWidth("350px");
        doctorSelect.setItemLabelGenerator(doctor -> doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName());
        doctorSelect.setItems(doctorService.findAll());

        divTextPatient.getElement().getStyle().set("font-size", "20px");
        divTextDoctor.getElement().getStyle().set("font-size", "20px");
        divTextDateTime.getElement().getStyle().set("font-size", "20px");

        dateTimePicker.setLabel("Выберите время визита");

        add(new H1("Создание визита пациента"));
        add(verticalLayout);
        if (appUserService.checkRoleForCurrentUser(RoleName.ROLE_READER)) {
            buttonFinal.setVisible(false);
        }
    }
}
