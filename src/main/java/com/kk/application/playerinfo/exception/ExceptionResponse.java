package com.kk.application.playerinfo.exception;

import lombok.*;

import java.util.*;
@AllArgsConstructor
@Data
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;

}
