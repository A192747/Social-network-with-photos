package org.oril.entities;

public class NotValidException extends RuntimeException{
    public NotValidException(String msg) {
        super(msg);
    }
}
