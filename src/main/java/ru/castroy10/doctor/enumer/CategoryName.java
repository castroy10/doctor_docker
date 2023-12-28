package ru.castroy10.doctor.enumer;

public enum CategoryName {
    THERAPIST("Терапевт"),
    GYNECOLOGIST("Гинеколог"),
    CARDIOLOGIST("Кардиолог"),
    OTOLARYNGOLOGIST("Отоларинголог"),
    OPHTHALMOLOGIST("Офтальмолог"),
    NEUROLOGIST("Невролог"),
    PEDIATRICIAN("Педиатр"),
    PSYCHIATRIST("Психиатр"),
    ENDOCRINOLOGIST("Эндокринолог"),
    SURGEON("Хирург"),
    DERMATOLOGIST("Дерматолог");

    private String title;

    CategoryName(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
