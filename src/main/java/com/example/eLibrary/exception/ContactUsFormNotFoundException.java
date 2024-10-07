package com.example.eLibrary.exception;

import java.util.NoSuchElementException;

public class ContactUsFormNotFoundException extends NoSuchElementException {
    public ContactUsFormNotFoundException(String message) {
        super(message);

    }
}
