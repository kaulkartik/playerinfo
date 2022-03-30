package com.kk.application.playerinfo.service;

import lombok.*;
import org.springframework.stereotype.*;

import java.text.*;
@Component
@NoArgsConstructor
public class DateHelperService {

    private static final java.text.SimpleDateFormat playerRecordDateFormat =
            new java.text.SimpleDateFormat("yyyy-MM-dd");

    private static final java.text.SimpleDateFormat playerRecordDateCreartionFormat =
            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String dateFormat;


    public boolean verifyDateFormat(String dateStr) {
        playerRecordDateFormat.setLenient(false);
        try {
            playerRecordDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean verifyDateTimeFormatOnCreation(String dateStr) {
        playerRecordDateCreartionFormat.setLenient(false);
        try {
            playerRecordDateCreartionFormat.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public StringBuilder beautifyDate(String dateStr) {
        return new StringBuilder(dateStr).append(" 00:00:00");
    }
}
