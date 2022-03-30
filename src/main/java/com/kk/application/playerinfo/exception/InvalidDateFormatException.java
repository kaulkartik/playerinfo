package com.kk.application.playerinfo.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateFormatException extends RuntimeException{
    String message;

    public InvalidDateFormatException(String message) {
        this.message = message;
    }
}
