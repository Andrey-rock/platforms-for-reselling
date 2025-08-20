package ru.skypro.homework.exceptions;

public class AdNotFoundException extends RuntimeException{
    public AdNotFoundException() {
        super("Объявление не найдено");
    }
}
