package ru.hzerr.exception.file;

public class HFileIsNotFileException extends HFileException {

    public HFileIsNotFileException() { super(); }
    public HFileIsNotFileException(String message) { super(message); }
    public HFileIsNotFileException(Exception e, String message) { super(e, message); }
}
