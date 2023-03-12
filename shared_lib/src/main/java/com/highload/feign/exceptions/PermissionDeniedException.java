package com.highload.feign.exceptions;

public class PermissionDeniedException extends Exception {
    public PermissionDeniedException(String message) {
        super(message);
    }
}
