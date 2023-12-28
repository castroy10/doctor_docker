package ru.castroy10.doctor.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.castroy10.doctor.enumer.RoleName;
import ru.castroy10.doctor.model.Visit;
import ru.castroy10.doctor.service.AppUserService;
import ru.castroy10.doctor.service.VisitService;

@PageTitle("View visit")
@Route("visit")
@PermitAll
public class VisitView extends VerticalLayout implements HasUrlParameter<Long> {

    private static final Logger log = LoggerFactory.getLogger(VisitView.class);
    private final VisitService visitService;
    private final AppUserService appUserService;
    Text patient = new Text("");
    TextArea textDiagnosis = new TextArea();
    TextArea textTreatment = new TextArea();
    TextArea textComment = new TextArea();
    Checkbox isFinished = new Checkbox();
    private final Button buttonVisitSave = new Button("Сохранить визит");
    private final Button buttonBack = new Button("Назад");
    Visit selectedVisit;
    private final VerticalLayout verticalLayout = new VerticalLayout(textDiagnosis, textTreatment, textComment, isFinished, buttonVisitSave, buttonBack);

    public VisitView(VisitService visitService, AppUserService appUserService) {
        this.visitService = visitService;
        this.appUserService = appUserService;
        createScene();

        buttonVisitSave.addClickListener(e -> {
            saveVisit();
        });

        buttonBack.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(MainView.class));
        });
    }

    private void createScene() {
        setAlignItems(Alignment.CENTER);
        add(new H1("Пациент"));
        add(new H1(patient));
        add(verticalLayout);
        textDiagnosis.setLabel("Диагноз");
        textTreatment.setLabel("Назначенное лечение");
        textComment.setLabel("Комментарий");
        isFinished.setLabel("Визит завершен");
        if (appUserService.checkRoleForCurrentUser(RoleName.ROLE_READER)) {
            buttonVisitSave.setVisible(false);
        }
    }

    private void saveVisit() {
        setValuesToVisit();
        if (visitService.save(selectedVisit)) {
            Notification.show("Прием успешно сохранен");
            log.info("Прием успешно сохранен id: " + selectedVisit.getId() + " пациент " + selectedVisit.getPatient().getFullName() + " доктор " + selectedVisit.getDoctor().getFullName());
        } else {
            Notification.show("Не удалось сохранить прием");
        }
    }

    private void setValuesToVisit() {
        selectedVisit.setDiagnosis(textDiagnosis.getValue());
        selectedVisit.setTreatment(textTreatment.getValue());
        selectedVisit.setComment(textComment.getValue());
        selectedVisit.setFinished(isFinished.getValue());
    }

    private void getVisit(Long id) {
        selectedVisit = visitService.findById(id);
        patient.setText(selectedVisit.getPatient().getFullName());
        if (selectedVisit.getDiagnosis() != null) textDiagnosis.setValue(selectedVisit.getDiagnosis());
        if (selectedVisit.getTreatment() != null) textTreatment.setValue(selectedVisit.getTreatment());
        if (selectedVisit.getComment() != null) textComment.setValue(selectedVisit.getComment());
        isFinished.setValue(selectedVisit.isFinished());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        getVisit(id);
    }
}
