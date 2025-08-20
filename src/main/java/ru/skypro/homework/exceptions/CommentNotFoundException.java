package ru.skypro.homework.exceptions;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(){
        super("комментарий не найден");
    }
}
