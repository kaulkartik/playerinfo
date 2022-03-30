package com.kk.application.playerinfo.dto;

import com.kk.application.playerinfo.entity.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import javax.validation.constraints.*;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlayerRecordResponseDto {

    Long id;

    String name;

    @Max(value = 1000, message = "The score should be greater than 1000.")
    @Min(value = 0, message = "The score should be greater than 0.")
    Integer score;

    String time;

    @Autowired
    public PlayerRecordResponseDto(PlayerRecordEntity playerRecordEntity) {
        this.id = playerRecordEntity.getPlayerId();
        this.name = playerRecordEntity.getName();
        this.score = playerRecordEntity.getScore();
        this.time = playerRecordEntity.getTime();
    }
}
