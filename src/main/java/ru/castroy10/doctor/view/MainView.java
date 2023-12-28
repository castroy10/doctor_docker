package ru.castroy10.doctor.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.castroy10.doctor.model.Doctor;
import ru.castroy10.doctor.model.Visit;
import ru.castroy10.doctor.security.SecurityService;
import ru.castroy10.doctor.service.AppUserService;
import ru.castroy10.doctor.service.VisitService;

import java.time.format.DateTimeFormatter;

@PageTitle("Main view")
@Route("")
@PermitAll
public class MainView extends VerticalLayout {

    private final AppUserService appUserService;
    private final VisitService visitService;
    private final SecurityService securityService;
    private static final Logger log = LoggerFactory.getLogger(MainView.class);
    private final Grid<Visit> currentVisits = new Grid<>(Visit.class, false);
    private final TextField filter = new TextField();
    private final Grid<Visit> searchVisits = new Grid<>(Visit.class, false);
    private final Button buttonVisit = new Button("Новый визит");
    private final Button buttonPatient = new Button("Новый пациент");
    private final Button buttonReport = new Button("Отчет");
    private final Button buttonLogout = new Button("Logout");
    private final HorizontalLayout horizontalLayout = new HorizontalLayout(buttonVisit, buttonPatient, buttonReport, buttonLogout);
    private final VerticalLayout verticalLayout = new VerticalLayout(currentVisits);
    private final VerticalLayout verticalLayoutSearch = new VerticalLayout(filter, searchVisits);

    public MainView(AppUserService appUserService, VisitService visitService, SecurityService securityService) {
        this.appUserService = appUserService;
        this.visitService = visitService;
        this.securityService = securityService;
        createScene();

        currentVisits.addItemClickListener(e -> {
            Visit visit = e.getItem();
            getUI().ifPresent(ui -> ui.navigate(VisitView.class, visit.getId()));
        });

        searchVisits.addItemClickListener(e -> {
            Visit visit = e.getItem();
            getUI().ifPresent(ui -> ui.navigate(VisitView.class, visit.getId()));
        });

        filter.addValueChangeListener(field -> {
            if (field.getValue().length() == 0) searchVisits.setVisible(false);
            if (field.getValue().length() > 2) {
                searchVisits.setItems(visitService.findByName(field.getValue().toLowerCase()));
                searchVisits.setVisible(true);
            }
        });

        buttonVisit.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(CreateVisitView.class));
        });

        buttonPatient.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(CreatePatientView.class));
        });

        buttonReport.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(CreateReportView.class));
        });

        buttonLogout.addClickListener(e -> {
            securityService.logout();
        });
    }

    private void createScene() {
        add(horizontalLayout);
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.END);

        setAlignItems(Alignment.CENTER);
        add(new H1("Информационная система Doctor"), new H1("Запланированные визиты:"), verticalLayout);
        currentVisits.setAllRowsVisible(true);
        currentVisits.addColumn(visit -> visit.getPatient().getFullName()).setHeader("Пациент").setAutoWidth(true).setSortable(true);
        currentVisits.addColumn(visit -> visit.getDoctor().getFullName()).setHeader("Доктор").setAutoWidth(true).setSortable(true);
        currentVisits.addColumn(visit -> visit.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).setHeader("Дата и время").setAutoWidth(true).setSortable(true);
        Doctor doctor = appUserService.getDoctorForLoginUser();
        currentVisits.setItems(visitService.findVisitIsNotFinishedForDoctor(doctor));

        filter.setPlaceholder("Поиск пациента. Минимум 3 символа");
        filter.setWidth("350px");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

        add(new H1("Поиск визитов"), verticalLayoutSearch);
        searchVisits.setVisible(false);
        searchVisits.addColumn(visit -> visit.getPatient().getFullName()).setHeader("Пациент").setAutoWidth(true).setSortable(true);
        searchVisits.addColumn(visit -> visit.getPatient().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).setHeader("Дата рождения").setAutoWidth(true).setSortable(true);
        searchVisits.addColumn(visit -> visit.getDoctor().getFullName()).setHeader("Доктор").setAutoWidth(true).setSortable(true);
        searchVisits.addColumn(visit -> visit.getDoctor().getCategory().stream().findFirst().get().getCategoryName()).setHeader("Категория").setAutoWidth(true).setSortable(true);
        searchVisits.addColumn(visit -> visit.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).setHeader("Дата и время").setAutoWidth(true).setSortable(true);
    }
}