package com.kk.application.playerinfo.entity;

import lombok.*;

import java.io.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRecordID implements Serializable {
    private String name;
    private String time;
}
