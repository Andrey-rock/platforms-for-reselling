-- liquibase formatted sql
-- changeset andrey-rock:1
CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');

CREATE TABLE пользователи
(
    id          INT primary key,
    логин       varchar(32) not null,
    пароль      varchar(16) not null,
    имя         varchar(32) not null,
    фамилия     varchar(32),
    телефон     varchar(16),
    роль        user_role,
    изображение varchar(255)
);

CREATE TABLE объявления
(
    Id_объявления INT primary key,
    описание      varchar(64),
    цена          INT         not null,
    заголовок     varchar(32) not null,
    изображение   varchar(255),
    author_id     INT         not null,
    FOREIGN KEY (author_id) REFERENCES пользователи (id) ON DELETE CASCADE
);

CREATE TABLE комментарии
(
    Id_комментария    INT primary key,
    время_создания    BIGINT,
    текст_комментария varchar(64),
    author_id         INT not null,
    FOREIGN KEY (author_id) REFERENCES пользователи (id) ON DELETE CASCADE
);