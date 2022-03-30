package com.kk.application.playerinfo.dto;

import com.kk.application.playerinfo.entity.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class IncomingRecordRequestDto {
    @NotNull(message = "Id cannot be null")
    Long id;

    @NotNull(message = "Name cannot be null")
    String name;

    @Max(value = 1000, message = "The score should be greater than 1000.")
    @Min(value = 0, message = "The score should be greater than 0.")
    @NotNull(message = "Score cannot be null")
    Integer score;

    @NotNull(message = "Id cannot be null")
    String time;


    @Autowired
    public PlayerRecordEntity convertToPlayerRecordEntity(){
        PlayerRecordEntity entity = new PlayerRecordEntity();
        //set entity values here from StudentDTO
        entity.setPlayerId(this.id);
        entity.setName(this.name);
        entity.setScore(this.score);
        entity.setTime(this.time);
        return entity ;
    }
}
