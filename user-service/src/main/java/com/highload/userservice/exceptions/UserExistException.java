package com.highload.userservice.exceptions;

public class UserExistException extends Exception {
    public UserExistException(String message) {
        super(message);
    }
}
