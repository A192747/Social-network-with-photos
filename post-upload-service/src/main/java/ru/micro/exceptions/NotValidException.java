package ru.micro.exceptions;

public class NotValidException extends RuntimeException{
    public NotValidException(String msg) {
        super(msg);
    }
}
