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
    Id_объявления   SERIAL primary key,
    описание        varchar(64),
    цена            INT         not null,
    заголовок       varchar(32) not null,
    изображение     varchar(255),
    id_пользователя INT         not null,
    FOREIGN KEY (id_пользователя) REFERENCES пользователи (id) ON DELETE CASCADE
);

CREATE TABLE комментарии
(
    Id_комментария    SERIAL primary key,
    время_создания    BIGINT,
    текст_комментария varchar(64),
    id_пользователя   INT not null,
    FOREIGN KEY (id_пользователя) REFERENCES пользователи (id) ON DELETE CASCADE,
    id_объявления     INT,
    FOREIGN KEY (id_объявления) REFERENCES объявления (Id_объявления) ON DELETE CASCADE
);

-- changeset andrey-rock:2

CREATE TABLE изображения
(
    id           SERIAL primary key,
    адрес_файла  varchar(64) not null,
    размер_файла BIGINT      not null,
    тип_файла    varchar(16) not null
);
