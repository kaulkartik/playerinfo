package com.kk.application.playerinfo.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PlayerDataCreationFailed extends RuntimeException {
    String message;

    public PlayerDataCreationFailed(String message) {
        this.message = message;
    }
}
