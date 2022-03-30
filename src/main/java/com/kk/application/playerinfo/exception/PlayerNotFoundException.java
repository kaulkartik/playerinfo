package com.kk.application.playerinfo.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends RuntimeException{
    String exceptionMessage;

    public PlayerNotFoundException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
