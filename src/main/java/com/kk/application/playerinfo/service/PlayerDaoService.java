package com.kk.application.playerinfo.service;

import com.kk.application.playerinfo.dto.*;
import com.kk.application.playerinfo.entity.*;
import com.kk.application.playerinfo.repos.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
public class PlayerDaoService {

    @Autowired
    PlayerRepository repository;
    private String pattern;

    @Autowired
    private DateHelperService dateHelperService;

    @Autowired
    private PlayerBusinessService playerBusinessService;

    /**
     * Method finds all the player records
     * @param page
     * @param pageSize
     * @param sortField
     * @return
     */
    public List<PlayerRecordResponseDto> findAllPlayersRecords(int page, int pageSize, String sortField){
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,sortField));
        List<PlayerRecordEntity> records = repository.findAll(paging).getContent();
        List<PlayerRecordResponseDto> responseList = records.stream()
                                                    .map(rec -> new PlayerRecordResponseDto(rec))
                                                    .collect(Collectors.toList());
        return responseList;
    }

    public void save(PlayerRecordEntity player) {

        repository.save(player);
    }

    /**
     * finds all the player records By name
     * @param name
     * @param page
     * @param pageSize
     * @param sortField
     * @return
     */
    public List<PlayerRecordResponseDto> findPlayerRecordsByName(String name, int page, int pageSize, String sortField) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,sortField));
        List<PlayerRecordEntity> records = repository.findByName(name, paging).getContent();
        List<PlayerRecordResponseDto> responseList = records.stream()
                                                    .map(rec -> new PlayerRecordResponseDto(rec))
                                                    .collect(Collectors.toList());
        return responseList;
    }

    /**
     * finds all the player records By Id
     * @param id
     * @param page
     * @param pageSize
     * @param sortField
     * @return
     */
    public List<PlayerRecordResponseDto> findPlayerRecordsById(Long id, int page, int pageSize, String sortField) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,sortField));
        List<PlayerRecordEntity> records = repository.findByPlayerId(id, paging).getContent();
        List<PlayerRecordResponseDto> responseList = records.stream()
                .map(rec -> new PlayerRecordResponseDto(rec))
                .collect(Collectors.toList());
        return responseList;
    }

    /**
     * Deletes a player
     * @param playerRecordEntity
     */
    public void deletePlayer(PlayerRecordEntity playerRecordEntity) {
         repository.delete(playerRecordEntity);
    }

    /**
     * Fetch top scores
     * @param playerName
     * @return
     */
    public Map<Integer, List<String> > fetchTopScores(String playerName){

        Map<Integer , List<String>> scoreMap = fetchAllScoresByPlayerName(playerName);
        Map<Integer , List<String>> topScoreMap = new HashMap<>();
        Integer topScore = repository.findByName(playerName)
                .stream()
                .map(scoreRecord -> scoreRecord.getScore())
                .reduce(Integer::max).get();

        List<String> highScoreTimes = scoreMap.get(topScore);
        topScoreMap.put(topScore, highScoreTimes);
        return topScoreMap;
    }

    public Map<Integer, List<String>> fetchWorstScores(String playerName){
        Map<Integer , List<String>> scoreMap = fetchAllScoresByPlayerName(playerName);
        Map<Integer , List<String>> lowScoreMap = new HashMap<>();
        Integer worstScore = repository.findByName(playerName)
                .stream()
                .map(scoreRecord -> scoreRecord.getScore())
                .reduce(Integer::min).get();

        List<String> highScoreTimes = scoreMap.get(worstScore);
        System.out.println("########################################" + scoreMap.get(worstScore));
        lowScoreMap.put(worstScore, highScoreTimes);
        return lowScoreMap;
    }

    public Map<Integer, List<String>> fetchAllScoresByPlayerName(String playerName){
        Map<Integer, List<String>> scoreMap = repository
                                            .findByName(playerName)
                                            .stream()
                                            .collect(Collectors.groupingBy(PlayerRecordEntity::getScore, Collectors.mapping(PlayerRecordEntity::getTime, Collectors.toList())));
        System.out.println(scoreMap);
        return scoreMap;

    }

    public Double fetchAverageScore(String playerName){
        // chnages score -> score
        return repository
                .findByName(playerName)
                .stream()
                .mapToDouble(score -> score.getScore())
                .average()
                .orElse(0.0);
    }

    public List<PlayerRecordResponseDto> findByPlayerRecordsAfter(String name, String targetDate, int page, int pageSize, String sortField) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,sortField));
        List<PlayerRecordEntity> records =  repository.findByName(name, paging).getContent();
        List<PlayerRecordEntity> filteredRecords = playerBusinessService.filterRecordsAfterSpecificDate(records, targetDate);
        List<PlayerRecordResponseDto> responseList = filteredRecords.stream().map(rec -> new PlayerRecordResponseDto(rec)).collect(Collectors.toList());
        return responseList;
    }

    public List<PlayerRecordResponseDto> findByPlayerRecordsBefore(String name, String targetDate, int page, int pageSize, String sortField) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,sortField));
        List<PlayerRecordEntity> records =  repository.findByName(name, paging).getContent();
        List<PlayerRecordResponseDto> response = playerBusinessService
                                                    .filterRecordsBeforeSpecificDate(records, targetDate)
                                                    .stream().map(rec -> new PlayerRecordResponseDto(rec))
                                                    .collect(Collectors.toList());
        return response;
    }

    public List<PlayerRecordResponseDto> findByPlayerRecordsBetween(String name, String startDate ,String targetDate, int page, int pageSize, String sortField) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,sortField));
        List<PlayerRecordEntity> records =  repository.findByName(name, paging).getContent();
        List<PlayerRecordEntity> recordsAfterSpecificDate =  playerBusinessService.filterRecordsAfterSpecificDate(records, startDate);
        List<PlayerRecordEntity> finalFilteredRecords =  playerBusinessService.filterRecordsBeforeSpecificDate(recordsAfterSpecificDate, targetDate);
        List<PlayerRecordResponseDto> response = finalFilteredRecords.stream().map(rec -> new PlayerRecordResponseDto(rec)).collect(Collectors.toList());
        return response;
    }

}
