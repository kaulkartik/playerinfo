package com.kk.application.playerinfo.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyQuerySetException extends RuntimeException{
    String exceptionMessage;

    public EmptyQuerySetException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
