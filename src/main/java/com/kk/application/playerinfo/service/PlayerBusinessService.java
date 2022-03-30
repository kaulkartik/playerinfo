package com.kk.application.playerinfo.service;

import com.kk.application.playerinfo.entity.*;
import org.springframework.stereotype.*;

import java.text.*;
import java.util.*;
import java.util.stream.*;

@Service
public class PlayerBusinessService {

    public List<PlayerRecordEntity> filterRecordsAfterSpecificDate(List<PlayerRecordEntity> records , String targetDate) {
        List<PlayerRecordEntity> sortedPlayerRecordEntities =
                records.stream().filter(
                        record -> {
                            try {
                                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(record.getTime())
                                        .after(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(targetDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                ).collect(Collectors.toCollection(ArrayList::new));
        return sortedPlayerRecordEntities;
    }

    public List<PlayerRecordEntity> filterRecordsBeforeSpecificDate(List<PlayerRecordEntity> records , String targetDate) {
        List<PlayerRecordEntity> sortedPlayerRecordEntities =
                records.stream().filter(
                        record -> {
                            try {
                                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(record.getTime())
                                        .before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(targetDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                ).collect(Collectors.toCollection(ArrayList::new));
        return sortedPlayerRecordEntities;
    }

    public List<PlayerRecordEntity> filterRecordsForSpecificPeriod(List<PlayerRecordEntity> records , String targetDate) {
        List<PlayerRecordEntity> sortedPlayerRecordEntities =
                records.stream().filter(
                        record -> {
                            try {
                                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(record.getTime())
                                        .before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(targetDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                ).collect(Collectors.toCollection(ArrayList::new));
        return sortedPlayerRecordEntities;
    }
}
