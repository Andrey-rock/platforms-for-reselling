-- liquibase formatted sql
-- changeset andrey-rock:1

CREATE TABLE пользователи
(
    id          SERIAL primary key,
    логин       varchar(32) not null,
    пароль      varchar(64) not null,
    имя         varchar(32) not null,
    фамилия     varchar(32),
    телефон     varchar(16),
    роль        varchar(32),
    изображение varchar(255)
);

CREATE TABLE объявления
(
    Id_объявления SERIAL primary key,
    описание      varchar(64),
    цена          INT         not null,
    заголовок     varchar(32) not null,
    изображение   varchar(255),
    author_id     INT         not null,
    FOREIGN KEY (author_id) REFERENCES пользователи (id) ON DELETE CASCADE
);

CREATE TABLE комментарии
(
    Id_комментария    SERIAL primary key,
    время_создания    BIGINT,
    текст_комментария varchar(64),
    author_id         INT not null,
    FOREIGN KEY (author_id) REFERENCES пользователи (id) ON DELETE CASCADE,
    ad_id             INT,
    FOREIGN KEY (ad_id) REFERENCES объявления (Id_объявления) ON DELETE CASCADE
);

-- changeset andrey-rock:2

CREATE TABLE users
(
    id       SERIAL primary key,
    username varchar(32) not null,
    password varchar(64) not null,
    enabled  BOOLEAN     not null
);

CREATE TABLE authorities
(
    id        SERIAL primary key,
    username  varchar(32) not null,
    authority varchar(64) not null
);