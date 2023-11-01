create table departments
(
    calc_setting float        null,
    id           bigint auto_increment
        primary key,
    name         varchar(255) null
);

create table daily_reports
(
    date            date        null,
    date_of_created datetime(6) null,
    department_id   bigint      null,
    id              bigint auto_increment
        primary key,
    constraint FK5kcdm2cb83v5l4kexa3luuk64
        foreign key (department_id) references departments (id)
);

create table employees
(
    active bit          null,
    id     bigint auto_increment
        primary key,
    name   varchar(255) null,
    constraint UK_sd5rcqkto9rwnmx8g16o41af
        unique (name)
);

create table employee_department
(
    department_id bigint not null,
    employee_id   bigint not null,
    constraint FK4cbu73xwprtxd3tpcop5w9chi
        foreign key (department_id) references departments (id),
    constraint FKeivrxorxedw0jroshkmeix9is
        foreign key (employee_id) references employees (id)
);

create table payments
(
    procent_from_sales int    null,
    tips               int    null,
    total_payment      int    null,
    daily_report_id    bigint null,
    employee_id        bigint null,
    id                 bigint auto_increment
        primary key,
    constraint FKgg9970yjb56tmui83b0dccqv5
        foreign key (employee_id) references employees (id),
    constraint FKlbndpgfuwidmeo25xqhg97494
        foreign key (daily_report_id) references daily_reports (id)
);

create table users
(
    active       bit          null,
    employee_id  bigint       null,
    id           bigint auto_increment
        primary key,
    email        varchar(255) null,
    name         varchar(255) null,
    password     varchar(255) null,
    phone_number varchar(255) null,
    constraint UK_6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UK_d1s31g1a7ilra77m65xmka3ei
        unique (employee_id),
    constraint FK6p2ib82uai0pj9yk1iassppgq
        foreign key (employee_id) references employees (id)
);

create table tokens
(
    expired    bit             null,
    revoked    bit             null,
    id         bigint auto_increment
        primary key,
    user_id    bigint          null,
    token      varchar(255)    null,
    token_type enum ('BEARER') null,
    constraint FK2dylsfo39lgjyqml2tbe0b0ss
        foreign key (user_id) references users (id)
);

create table user_role
(
    user_id bigint                                      not null,
    roles   enum ('ADMIN', 'MANAGER', 'MASTER', 'USER') null,
    constraint FKj345gk1bovqvfame88rcx7yyx
        foreign key (user_id) references users (id)
);

create table wishes
(
    end_time      time(6)                      null,
    fixed         bit                          null,
    date_time     datetime(6)                  null,
    department_id bigint                       null,
    employee_id   bigint                       null,
    id            bigint auto_increment
        primary key,
    type          enum ('DAY_OFF', 'DAY_WORK') null,
    constraint FKg8ed6sstml4009juci7bihd29
        foreign key (department_id) references departments (id),
    constraint FKoaakwe2qsih2lrhcnp9g6xr3c
        foreign key (employee_id) references employees (id)
);



