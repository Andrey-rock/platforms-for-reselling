package ru.skypro.homework.exceptions;

public class UserDoesNotExistException extends RuntimeException{
    public UserDoesNotExistException(String message){
        super(message);
    }
}
