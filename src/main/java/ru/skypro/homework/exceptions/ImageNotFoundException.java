package ru.skypro.homework.exceptions;

public class ImageNotFoundException extends RuntimeException{
    public ImageNotFoundException(){
        super("картинка не найдена");
    }
}
