package com.url.shortner.exception;

public class LongUrlNotFoundException extends RuntimeException {
    public LongUrlNotFoundException() {
    }

    public LongUrlNotFoundException(String message) {
        super(message);
    }
}
