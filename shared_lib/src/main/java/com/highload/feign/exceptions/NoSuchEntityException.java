package com.highload.feign.exceptions;

public class NoSuchEntityException extends Exception {
    public NoSuchEntityException(String message) {
        super(message);
    }
}
