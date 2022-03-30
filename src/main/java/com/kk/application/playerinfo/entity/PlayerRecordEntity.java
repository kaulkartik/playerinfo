package com.kk.application.playerinfo.entity;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@IdClass(PlayerRecordID.class)
@Table(name = "player")
public class PlayerRecordEntity {

    @Column(name = "player_id")
    Long playerId;

    /*Player name field*/
    @Column(name = "player_name")
    @Id
    String name;

    @Max(value = 1000, message = "The score should be greater than 1000.")
    @Min(value = 0, message = "The score should be greater than 0.")
    @Column(name = "score")
    Integer score;

    @Id
    @Column(name = "record_time")
    String time;
}
