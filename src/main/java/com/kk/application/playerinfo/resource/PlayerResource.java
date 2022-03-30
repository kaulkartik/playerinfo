package com.kk.application.playerinfo.resource;

import com.kk.application.playerinfo.constant.*;
import com.kk.application.playerinfo.dto.*;
import com.kk.application.playerinfo.entity.*;
import com.kk.application.playerinfo.exception.*;
import com.kk.application.playerinfo.repos.*;
import com.kk.application.playerinfo.service.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.*;

import javax.validation.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;

@RestController
public class PlayerResource {
    private static final Logger logger = LoggerFactory.getLogger(PlayerResource.class);

    @Autowired
    PlayerDaoService playerDaoService;

    @Autowired
    DateHelperService dateHelperService;

    @Autowired
    PlayerRepository repository;

    /**
     *  T
     * @param page
     * @param size
     * @param sortField
     * @return
     */
    @GetMapping(URIConstants.GET_ALL_PLAYERS_FULL_RECORD)
    public ResponseEntity<List<PlayerRecordResponseDto>> retrieveAllPlayersRecords(
            @RequestParam(required = false, name = "page",
                    defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                    defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                    defaultValue = "time") String sortField)
    {
        logger.info("[Start] Fetching all the players");
        List<PlayerRecordResponseDto> response = playerDaoService.findAllPlayersRecords(page, size, sortField);
        return ResponseEntity.ok(response);
    }

    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_FULL_RECORD_BY_PLAYER_NAME)
    public ResponseEntity<List<PlayerRecordResponseDto>> retrievePlayerRecordsByPlayerName(
            @PathVariable String name,
            @RequestParam(required = false, name = "page",
                   defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                    defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                    defaultValue = "time") String sortField)
    {
        List<PlayerRecordResponseDto> response = playerDaoService.findPlayerRecordsByName(name.toLowerCase() ,page, size, sortField);
        if (Optional.ofNullable(response).isEmpty()) {
            throw new PlayerNotFoundException("Searched Player does not exists");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_FULL_RECORD_BY_PLAYER_ID)
    public ResponseEntity<List<PlayerRecordResponseDto>> retrievePlayerRecordsById(
            @PathVariable Long id,
            @RequestParam(required = false, name = "page",
                    defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                    defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                    defaultValue = "time") String sortField)
    {
        List<PlayerRecordResponseDto> response = playerDaoService.findPlayerRecordsById(id ,page, size, sortField);
        if (Optional.ofNullable(response).isEmpty()) {
            throw new PlayerNotFoundException("Searched Player does not exists");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(URIConstants.GET_MULTIPLE_PLAYER_FULL_RECORD_BY_PLAYER_NAME)
    public ResponseEntity<List<PlayerRecordResponseDto>> retrieveMultiplePlayersRecordsByPlayerName(
            @RequestParam(required = true,defaultValue = "")
                    List<String> names,
            @RequestParam(required = false, name = "page",
                    defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                    defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                    defaultValue = "time") String sortField
    ) {
        if(Optional.of(names).isEmpty()) {
            throw new EmptyQuerySetException("Need to pass at least one player to query");
        }
        List<List<PlayerRecordResponseDto>> result = new ArrayList<>();
        for (String playerName : names) {
            List<PlayerRecordResponseDto> playerRecords = playerDaoService.findPlayerRecordsByName(playerName.toLowerCase(),page, size, sortField);
            if (playerRecords == null || playerRecords.size()==0) {
                continue;
            }
            result.add(playerRecords);
        }
        List<PlayerRecordResponseDto> formattedResponse = result
                                            .stream()
                                            .flatMap(List::stream)
                                            .collect(Collectors.toList());
    // To do Hateos support
        return ResponseEntity.ok(formattedResponse);
    }

    @GetMapping(URIConstants.GET_MULTIPLE_PLAYER_FULL_RECORD_BY_PLAYER_ID)
    public ResponseEntity<List<PlayerRecordResponseDto>> retrieveMultiplePlayersRecordsByIds(
            @RequestParam List<Long>id,
            @RequestParam(required = false, name = "page",
                    defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                    defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                    defaultValue = "time") String sortField
    ) {
        List<List<PlayerRecordResponseDto>> result = new ArrayList<>();
        for (Long playerId : id) {
            List<PlayerRecordResponseDto> playerRecords = playerDaoService.findPlayerRecordsById(playerId ,page, size, sortField);
            if (playerRecords == null || playerRecords.size()==0) {
                continue;
            }

            result.add(playerRecords);
        }
        List<PlayerRecordResponseDto> formattedResponse = result
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        // To do Hateos support
        return ResponseEntity.ok(formattedResponse);
    }

    @PostMapping(URIConstants.SAVE_INDIVIDUAL_PLAYER_RECORD)
    public ResponseEntity<PlayerRecordEntity> savePlayerRecord(@Valid @RequestBody IncomingRecordRequestDto playerCreationRequest) {
        playerCreationRequest.setName(playerCreationRequest.getName().toLowerCase());
        if(!dateHelperService.verifyDateTimeFormatOnCreation(playerCreationRequest.getTime())) {
            throw new InvalidDateFormatException( "Wrong record entry time format . Change to yyyy-mm-dd hh:mm:ss " +
                                                    "Ex.2022-03-30 09:05:11");
        }
        PlayerRecordEntity playerRecordEntity = playerCreationRequest.convertToPlayerRecordEntity();
        try{
            playerDaoService.save(playerRecordEntity);
        } catch (PlayerNotFoundException exception) {
            return ResponseEntity.internalServerError().build();
        }
        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("name")
                        .buildAndExpand(playerRecordEntity.getName())
                        .toUri();
        return ResponseEntity.created(location).build();

    }

    @DeleteMapping(URIConstants.DELETE_PLAYER_RECORDS_BY_PLAYER_NAME)
    public ResponseEntity<?> deletePlayerAllRecordsByName (@PathVariable String name) {
        List<PlayerRecordEntity> playerRecordEntities = repository.findByName(name);
        if (Optional.ofNullable(playerRecordEntities).isEmpty()) {
            throw new PlayerNotFoundException("Player not found!");
        }
        playerRecordEntities.stream().forEach((record) ->{
            //PlayerId id = new PlayerId(record.getId(), record.getTime());
            playerDaoService.deletePlayer(record);
        });
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(URIConstants.DELETE_PLAYER_RECORDS_BY_PLAYER_ID)
    public ResponseEntity<?> deletePlayerAllRecordsById (@PathVariable Long id) {
        List<PlayerRecordEntity> playerRecordEntities = repository.findByPlayerId(id);
        if (Optional.ofNullable(playerRecordEntities).isEmpty()) {
            throw new PlayerNotFoundException("Player not found!");
        }
        playerRecordEntities.stream().forEach((record) ->{
            //PlayerId id = new PlayerId(record.getId(), record.getTime());
            playerDaoService.deletePlayer(record);
        });
        return ResponseEntity.accepted().build();
    }


    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_TOP_SCORE_TIME_MAPPED_RECORDS)
    public Map<Integer, List<String>> retrievePlayerTopScore(@PathVariable String name) {
        List<PlayerRecordEntity> playerRecordEntities = repository.findByName(name);
        if (Optional.ofNullable(playerRecordEntities).isEmpty()) {
            throw new PlayerNotFoundException("Player not found!");
        }
        return playerDaoService.fetchTopScores(name);
    }

    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_ALL_SCORE_TIME_MAPPED_RECORDS)
    public Map<Integer, List<String>> retrievePlayerAllScore(@PathVariable String name) {
        List<PlayerRecordEntity> playerRecordEntities = repository.findByName(name);
        if (Optional.ofNullable(playerRecordEntities).isEmpty()) {
            throw new PlayerNotFoundException("Player not found!");
        }
        return playerDaoService.fetchAllScoresByPlayerName(name);
    }

    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_ALL_TIME_AVERAGE_SCORE_RECORD)
    public Double retrievePlayerAverageScore(@PathVariable String name) {
        List<PlayerRecordEntity> playerRecordEntities = repository.findByName(name);
        if (Optional.ofNullable(playerRecordEntities).isEmpty()) {
            throw new PlayerNotFoundException("Player not found!");
        }
        return playerDaoService.fetchAverageScore(name);
    }

    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_WORST_SCORE_TIME_MAPPED_RECORDS)
    public Map<Integer, List<String>> retrievePlayerWorstScore(@PathVariable String name) {
        List<PlayerRecordEntity> playerRecordEntities = repository.findByName(name);
        if (Optional.ofNullable(playerRecordEntities).isEmpty()) {
            throw new PlayerNotFoundException("Player not found!");
        }
        return playerDaoService.fetchWorstScores(name);
    }

    /**
     *
     * VALID DATE FORMAT : YYYY-MM-DD
     */
    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_SCORE_BETWEEN_GIVEN_PERIOD_RECORDS_BY_PLAYER_NAME)
    public ResponseEntity<List<PlayerRecordResponseDto>> retrievePlayerScoresForPeriod(
            @PathVariable String name,
            @PathVariable String startDate,
            @PathVariable String targetDate,
            @RequestParam(required = false, name = "page",
                   defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                   defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                   defaultValue = "time") String sortField
    ) {
        List<PlayerRecordResponseDto> playerRecords = playerDaoService.findPlayerRecordsByName(name.toLowerCase(), page, size, sortField);
        if (Optional.ofNullable(playerRecords).isEmpty()) {
            throw new PlayerNotFoundException("Searched Player does not exists");
        }
        else if(!dateHelperService.verifyDateFormat(startDate) ||
                !dateHelperService.verifyDateFormat(targetDate)){
            throw new InvalidDateFormatException("Invalid date format..");
        }

        startDate = dateHelperService.beautifyDate(startDate).toString();
        targetDate = dateHelperService.beautifyDate(targetDate).toString();

        List<PlayerRecordResponseDto> response =  playerDaoService.findByPlayerRecordsBetween(name, startDate, targetDate, page, size, sortField);
        return ResponseEntity.ok(response);
    }

    /**
     * VALID DATE FORMAT : YYYY-MM-DD
     */
    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_SCORE_RECORDS_SINCE_BY_PLAYER_NAME)
    public ResponseEntity<List<PlayerRecordResponseDto>>  retrievePlayerScoresSince(
            @PathVariable String name,
            @PathVariable String targetDate,
            @RequestParam(required = false, name = "page",
                    defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                    defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                    defaultValue = "time") String sortField
    ) {
        List<PlayerRecordResponseDto> playerRecordResponse = playerDaoService.findPlayerRecordsByName(name.toLowerCase(), page, size, sortField);
        if (Optional.ofNullable(playerRecordResponse).isEmpty()) {
            throw new PlayerNotFoundException("Searched Player does not exists");
        }
        else if(!dateHelperService.verifyDateFormat(targetDate)){
            throw new InvalidDateFormatException("Invalid date format..");
        }
        targetDate = dateHelperService.beautifyDate(targetDate).toString();
        logger.info("***************** " + targetDate);
        List<PlayerRecordResponseDto> response = playerDaoService.findByPlayerRecordsAfter(name, targetDate,page, size, sortField);
        return ResponseEntity.ok(response);
    }

    /**
     * VALID DATE FORMAT : YYYY-MM-DD
     */
    @GetMapping(URIConstants.GET_INDIVIDUAL_PLAYER_SCORE_RECORDS_BEFORE_SPECIFIC_DATE__BY_PLAYER_NAME)
    public ResponseEntity<List<PlayerRecordResponseDto>> retrievePlayerScoresBefore(
            @PathVariable String name,
            @PathVariable String targetDate,
            @RequestParam(required = false, name = "page",
                    defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                    defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                    defaultValue = "time") String sortField) {
        List<PlayerRecordResponseDto> playerRecords = playerDaoService.findPlayerRecordsByName(name.toLowerCase(),page, size, sortField);
        if (Optional.ofNullable(playerRecords).isEmpty()) {
            throw new PlayerNotFoundException("Searched Player does not exists");
        }
        else if(!dateHelperService.verifyDateFormat(targetDate)){
            throw new InvalidDateFormatException("Invalid date format..");
        }
        targetDate = dateHelperService.beautifyDate(targetDate).toString();
        List<PlayerRecordResponseDto>  response = playerDaoService.findByPlayerRecordsBefore(name, targetDate,page, size, sortField);
        return ResponseEntity.ok(response);
    }

    /**
     *
      * VALID DATE FORMAT : YYYY-MM-DD
     */
    @GetMapping(URIConstants.GET_MULTIPLE_PLAYER_SCORE_RECORDS_BEFORE_SPECIFIC_DATE__BY_PLAYER_NAME)
    public ResponseEntity<?> retrievePlayerBulkScoresBefore(
            @PathVariable String targetDate,
            @RequestParam (required = true, defaultValue = "")List<String>names,
            @RequestParam(required = false, name = "page",
                    defaultValue = "0") int page,
            @RequestParam(required = false, name = "size",
                    defaultValue = "20") int size,
            @RequestParam(required = false, name = "sortField",
                    defaultValue = "time") String sortField
    ) {
        if (Optional.of(names).isEmpty()) {
            throw new EmptyQuerySetException("Need to pass at least one player to query");
        }
        List<List<PlayerRecordResponseDto>> result = new ArrayList<>();
        if (!dateHelperService.verifyDateFormat(targetDate)) {
            throw new InvalidDateFormatException("Invalid date format..");
        }

        for (String playerName : names) {
            List<PlayerRecordResponseDto> playerRecords = playerDaoService.findPlayerRecordsByName(playerName.toLowerCase(), page, size, sortField);
            if (playerRecords == null || playerRecords.size() == 0) {
                continue;
            }
            targetDate = dateHelperService.beautifyDate(targetDate).toString();
            List<PlayerRecordResponseDto> response = playerDaoService.findByPlayerRecordsBefore(playerName, targetDate, page, size, sortField);
            result.add(response);
        }
        return ResponseEntity.ok(result);
    }

}