package ru.castroy10.doctor.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.castroy10.doctor.enumer.RoleName;
import ru.castroy10.doctor.model.Patient;
import ru.castroy10.doctor.model.Visit;
import ru.castroy10.doctor.service.AppUserService;
import ru.castroy10.doctor.service.PatientService;

import java.util.List;

@PageTitle("Create patient")
@Route("createpatient")
@PermitAll
public class CreatePatientView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(CreatePatientView.class);
    private final AppUserService appUserService;
    private final PatientService patientService;
    private final TextField filter = new TextField();
    private final Grid<Patient> patientGrid = new Grid<>(Patient.class, false);
    private final TextField lastName = new TextField();
    private final TextField firstName = new TextField();
    private final TextField middleName = new TextField();
    private final TextField mobilePhone = new TextField();
    private final TextField email = new TextField();
    private final DatePicker datePicker = new DatePicker();
    private final Button buttonSave = new Button("Сохранить пациента");
    private final Button buttonBack = new Button("Назад");
    private Patient patient = new Patient();
    private final VerticalLayout verticalLayout = new VerticalLayout(filter, patientGrid, lastName, firstName, middleName, datePicker, mobilePhone, email, buttonSave, buttonBack);

    public CreatePatientView(AppUserService appUserService, PatientService patientService) {
        this.appUserService = appUserService;
        this.patientService = patientService;
        createScene();

        filter.addValueChangeListener(field -> {
            if (field.getValue().length() == 0) patientGrid.setVisible(false);
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
            patient = e.getItem();
            setPatientFromGrid();
        });

        buttonSave.addClickListener(e -> {
            Patient patient = getPatientFromFields();
            if (patient.getLastName() != null &&
                    patient.getFirstName() != null &&
                    patient.getMiddleName() != null &&
                    patient.getDate() != null &&
                    patient.getMobilePhone() != null &&
                    patientService.save(patient)) {
                Notification.show("Пациент успешно создан");
                log.info("Пациент успешно создан id: " + patient.getId() + " " + patient.getFullName());
                cleanFields();
            } else {
                Notification.show("Не удалось создать пациента");
            }
        });

        buttonBack.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(MainView.class));
        });
    }

    private void createScene() {
        setAlignItems(Alignment.CENTER);
        patientGrid.setAllRowsVisible(true);

        lastName.setLabel("Фамилия");
        firstName.setLabel("Имя");
        middleName.setLabel("Отчество");
        datePicker.setLabel("Дата рождения");
        mobilePhone.setLabel("Мобильный телефон");
        mobilePhone.setPattern("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{1,3})[- .]?\\d{3}[- .]?\\d{2}[- .]?\\d{2}$");
        mobilePhone.setAllowedCharPattern("[0-9()+- ]");
        email.setLabel("Email");
        email.setPattern("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");

        filter.setPlaceholder("Поиск пациента. Минимум 3 символа");
        filter.setWidth("350px");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

        patientGrid.setVisible(false);
        patientGrid.addColumn(patient -> patient.getLastName()).setHeader("Фамилия").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getFirstName()).setHeader("Имя").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getMiddleName()).setHeader("Отчество").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getDate()).setHeader("Дата рождения").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getMobilePhone()).setHeader("Телефон").setAutoWidth(true).setSortable(true);
        patientGrid.addColumn(patient -> patient.getEmail()).setHeader("Email").setAutoWidth(true).setSortable(true);

        add(new H1("Создание пациента"));
        add(verticalLayout);
        if (appUserService.checkRoleForCurrentUser(RoleName.ROLE_READER)) {
            buttonSave.setVisible(false);
        }
    }

    private void setPatientFromGrid() {
        lastName.setValue(patient.getLastName());
        firstName.setValue(patient.getFirstName());
        middleName.setValue(patient.getMiddleName());
        datePicker.setValue(patient.getDate());
        mobilePhone.setValue(patient.getMobilePhone());
        email.setValue(patient.getEmail());
    }

    private Patient getPatientFromFields() {
        patient.setLastName(lastName.getValue());
        patient.setFirstName(firstName.getValue());
        patient.setMiddleName(middleName.getValue());
        patient.setDate(datePicker.getValue());
        patient.setMobilePhone(mobilePhone.getValue());
        patient.setEmail(email.getValue());
        return patient;
    }

    private void cleanFields() {
        lastName.clear();
        firstName.clear();
        middleName.clear();
        datePicker.clear();
        mobilePhone.clear();
        email.clear();
        patient = new Patient();
    }
}
