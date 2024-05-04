package com.katyshev.webZakat.exceptions;

public class WrongFileException extends RuntimeException{
    public WrongFileException(String message) {
        super(message);
    }
}
