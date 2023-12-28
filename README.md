# Информационная система Doctor
#### Учебная информационная система учреждения здравоохранения для записи и приема пациентов
#### 50 докторов, 10 000 пациентов, 50 000 визитов
Использованные технологии:
- Spring Boot
- Vaadin Framework
- Postgres SQL
- Spring Security

Посмотреть, как это работает можно здесь: https://doctor.castroy10.ru/  
Логин: doctor3, пароль: 12345

#### Сборка и запуск в Docker
После скачивания проекта необходимо создать в корневой папке проекта файл .env, в котором указать значения c которым запустится Postgres SQL и будет отправляться email.  
POSTGRES_USERNAME=  
POSTGRES_PASSWORD=  
SMTP_SERVER=  
EMAIL_USERNAME=  
EMAIL_PASSWORD=  
Проект собирается и запускается последовательностью команд:

    git clone https://github.com/castroy10/doctor_docker
    cd doctor_docker
    docker-compose up -d

После этого приложение соберется в контейнер, подключит контейнер базы данных с данными и будет доступно по адресу http://localhost:8080

##### База данных состоит из следующих таблиц:

    create table doctor(
    id integer not null generated by default as identity PRIMARY KEY,
    last_name varchar(255),
    first_name varchar(255),
    middle_name varchar(255),
    email varchar (255)
    );
    
    create table category(
    id integer not null generated by default as identity PRIMARY KEY,
    category_name varchar(255)
    );
    
    create table doctor_category(
    doctor_id integer references doctor,
    category_id integer references category
    );
    
    create table patient(
    id integer not null generated by default as identity PRIMARY KEY,
    last_name varchar(255),
    first_name varchar(255),
    middle_name varchar(255),
    mobile_phone varchar(255),
    email varchar(255),
    birthday date
    );
    
    create table visit(
    id integer not null generated by default as identity PRIMARY KEY,
    patient_id integer,
    date_time timestamp,
    diagnosis text,
    treatment text,
    comment text,
    isFinished bool,
    doctor_id integer,
    constraint fk_doctor foreign key (doctor_id) references doctor(id),
    constraint fk_patient foreign key (patient_id) references patient(id)
    );
    
    create table appuser(
    id integer not null generated by default as identity PRIMARY KEY,
    username varchar(100) not null,
    password varchar(100) not null,
    account_nonexpired boolean,
    account_nonlocked boolean,
    credentials_nonexpired boolean,
    is_enabled boolean,
    doctor_id integer,
    constraint fk_doctor foreign key (doctor_id) references doctor(id)
    );
    
    create table role(
    id integer not null generated by default as identity PRIMARY KEY,
    role_name varchar(255)
    );
    
    create table appuser_role(
    appuser_id integer references appuser,
    role_id integer references role
    );