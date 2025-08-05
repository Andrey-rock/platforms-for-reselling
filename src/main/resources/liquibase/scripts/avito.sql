-- liquibase formatted sql
-- changeset andrey-rock:1
CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');

CREATE TABLE Пользователи
(
    id          INT primary key,
    логин       varchar(32) not null ,
    пароль      varchar(16) not null ,
    имя         varchar(32) not null ,
    фамилия     varchar(32),
    телефон     varchar(16),
    роль        user_role,
    изображение varchar(255)
);

CREATE TABLE Объявления
(
    id          INT primary key,
    описание    varchar(64),
    цена        INT not null ,
    заголовок   varchar(32) not null ,
    изображение varchar(255),
    author      INT not null ,
    FOREIGN KEY (author) REFERENCES Пользователи (id) ON DELETE CASCADE
);

CREATE TABLE Комментарии
(
    id                INT primary key,
    время_создания    INT,
    текст_комментария varchar(64),
    author            INT not null,
    FOREIGN KEY (author) REFERENCES Пользователи (id)
);