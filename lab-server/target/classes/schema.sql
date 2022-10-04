create table person
(
    id serial,
    "name" varchar not null,
    coordinates_id int not null,
    "creationDate" timestamp not null,
    height int not null,
    "birthDay" timestamp,
    weight real not null,
    hair_color_id int not null,
    location_id int not null,
    user_id int not null
);

create unique index person_id_uindex
    on person (id);

create table coordinates
(
    id serial
        constraint coordinates_pk
            primary key,
    x real,
    y bigint
);

create table color
(
    id serial
        constraint color_pk
            primary key,
    "name" varchar not null
);

create table location
(
    id serial
        constraint chapter_pk
            primary key,
    "name" varchar not null,
    x int not null,
    y int not null,
    z bigint
);

create table "user"
(
    id serial
        constraint user_pk
            primary key,
    "name" varchar not null,
    password varchar not null
);

INSERT INTO "color" (id, name) VALUES (DEFAULT, 'GREEN');
INSERT INTO "color" (id, name) VALUES (DEFAULT, 'YELLOW');
INSERT INTO "color" (id, name) VALUES (DEFAULT, 'ORANGE');

