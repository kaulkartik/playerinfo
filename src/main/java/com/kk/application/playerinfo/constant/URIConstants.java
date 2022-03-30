package com.kk.application.playerinfo.constant;

public class URIConstants {
    public static final String  GET_ALL_PLAYERS_FULL_RECORD = "/dot/com/v1/records/players";
    public static final String  GET_INDIVIDUAL_PLAYER_FULL_RECORD_BY_PLAYER_NAME = "/dot/com/v1/records/players/{name}";
    public static final String  GET_INDIVIDUAL_PLAYER_FULL_RECORD_BY_PLAYER_ID = "/dot/com/v2/records/players/{id}";
    public static final String  GET_MULTIPLE_PLAYER_FULL_RECORD_BY_PLAYER_NAME = "/dot/com/v1/records/bulk/players";
    public static final String  GET_MULTIPLE_PLAYER_FULL_RECORD_BY_PLAYER_ID = "/dot/com/v2/records/bulk/players";
    public static final String  SAVE_INDIVIDUAL_PLAYER_RECORD = "/dot/com/v1/player/record";
    public static final String  DELETE_PLAYER_RECORDS_BY_PLAYER_NAME = "/dot/com/v1/players/{name}/records";
    public static final String  DELETE_PLAYER_RECORDS_BY_PLAYER_ID ="/dot/com/v2/players/{id}/records";
    public static final String  GET_INDIVIDUAL_PLAYER_TOP_SCORE_TIME_MAPPED_RECORDS = "/dot/com/v1/players/{name}/topscores";
    public static final String  GET_INDIVIDUAL_PLAYER_ALL_SCORE_TIME_MAPPED_RECORDS ="/dot/com/v1/players/{name}/allScores";
    public static final String  GET_INDIVIDUAL_PLAYER_ALL_TIME_AVERAGE_SCORE_RECORD = "/dot/com/v1/players/{name}/averagescore";
    public static final String  GET_INDIVIDUAL_PLAYER_WORST_SCORE_TIME_MAPPED_RECORDS ="/dot/com/v1/players/{name}/lowestscores";
    public static final String  GET_INDIVIDUAL_PLAYER_SCORE_BETWEEN_GIVEN_PERIOD_RECORDS_BY_PLAYER_NAME = "/dot/com/v1/players/{name}/scores/from/{startDate}/to/{targetDate}";
    public static final String  GET_INDIVIDUAL_PLAYER_SCORE_RECORDS_SINCE_BY_PLAYER_NAME = "/dot/com/v1/players/{name}/scores/since/{targetDate}";
    public static final String  GET_INDIVIDUAL_PLAYER_SCORE_RECORDS_BEFORE_SPECIFIC_DATE__BY_PLAYER_NAME ="/dot/com/v1/players/{name}/scores/before/{targetDate}";
    public static final String  GET_MULTIPLE_PLAYER_SCORE_RECORDS_BEFORE_SPECIFIC_DATE__BY_PLAYER_NAME = "/dot/com/v1/players/bulk/scores/before/{targetDate}";
}
