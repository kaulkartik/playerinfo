package com.kk.application.playerinfo.resource;

import com.fasterxml.jackson.databind.*;

import com.kk.application.playerinfo.dto.*;
import com.kk.application.playerinfo.entity.*;
import com.kk.application.playerinfo.exception.*;
import com.kk.application.playerinfo.repos.*;
import com.kk.application.playerinfo.service.*;

import org.junit.jupiter.api.*;


import org.mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;


@SpringBootTest
@AutoConfigureMockMvc
class PlayerResourceTest {

    @MockBean
    PlayerDaoService mockPlayerDaoService;

    @MockBean
    DateHelperService mockDateHelperService;

    @MockBean
    PlayerRepository mockpPayerRepository;


    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    PlayerResource mockPlayerResource;

    @Autowired
    ObjectMapper mapper;

    PlayerRecordResponseDto RECORD_1 = PlayerRecordResponseDto
            .builder()
            .id(1L)
            .name("testPlayer1")
            .time("2022-03-30 10:03:50")
            .score(40)
            .build();
    PlayerRecordResponseDto RECORD_2 = PlayerRecordResponseDto
            .builder()
            .id(2L)
            .name("testPlayer2")
            .time("2022-03-30 10:13:50")
            .score(30)
            .build();

    PlayerRecordResponseDto RECORD_3 = PlayerRecordResponseDto
            .builder()
            .id(3L)
            .name("testPlayer3")
            .time("2022-03-30 10:33:50")
            .score(2)
            .build();

    PlayerRecordResponseDto RECORD_4 = PlayerRecordResponseDto
            .builder()
            .id(1L)
            .name("testPlayer1")
            .time("2021-03-30 10:33:50")
            .score(21)
            .build();

    PlayerRecordResponseDto RECORD_5 = PlayerRecordResponseDto
            .builder()
            .id(1L)
            .name("testPlayer1")
            .time("2018-03-30 12:33:50")
            .score(77)
            .build();

    PlayerRecordEntity ENTITY_RECORD_1 = PlayerRecordEntity
            .builder()
            .playerId(1L)
            .name("testPlayer1")
            .time("2022-03-30 10:03:50")
            .score(40)
            .build();
    PlayerRecordEntity ENTITY_RECORD_2 = PlayerRecordEntity
            .builder()
            .playerId(2L)
            .name("testPlayer2")
            .time("2022-03-30 10:13:50")
            .score(30)
            .build();

    PlayerRecordEntity ENTITY_RECORD_3 = PlayerRecordEntity
            .builder()
            .playerId(3L)
            .name("testPlayer3")
            .time("2022-03-30 10:33:50")
            .score(2)
            .build();

    PlayerRecordEntity ENTITY_RECORD_4 = PlayerRecordEntity
            .builder()
            .playerId(1L)
            .name("testPlayer1")
            .time("2021-03-30 10:33:50")
            .score(21)
            .build();

    PlayerRecordEntity ENTITY_RECORD_5 = PlayerRecordEntity
            .builder()
            .playerId(1L)
            .name("testPlayer1")
            .time("2018-03-30 12:33:50")
            .score(77)
            .build();

    List<String> scoreList_1 = new ArrayList<>();
    List<String> scoreList_2 = new ArrayList<>();
    List<String> scoreList_3 = new ArrayList<>();

    HashMap<Integer, List<String>> scoreMap_1 = new HashMap<>();


    @Test
    void testRetrieveAllPlayersForNonEmptyResponse() throws Exception {

        List<PlayerRecordResponseDto> testResponse = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        when(mockPlayerDaoService.findAllPlayersRecords(anyInt(), anyInt(), anyString())).thenReturn(testResponse);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/records/players")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testResponse));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("testPlayer1"))
                .andExpect(jsonPath("$[0].score").value("40"))
                .andExpect(jsonPath("$[0].time").value("2022-03-30 10:03:50"))
                .andReturn();

    }

    @Test
    void testRetrieveAllPlayersForEmptyResponse() throws Exception {

        List<PlayerRecordResponseDto> testResponse = new ArrayList<>();
        when(mockPlayerDaoService.findAllPlayersRecords(anyInt(), anyInt(), anyString())).thenReturn(testResponse);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/records/players")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testResponse));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
    }


    @Test
    void testTopScoreBySinglePlayerForNonEmptyResponse_singleTopScore() throws Exception {
        scoreList_1.add("2021-03-30 10:33:50");
        Integer topScore = 70;
        scoreMap_1.put(topScore, scoreList_1);
        when(mockPlayerDaoService.fetchTopScores(anyString())).thenReturn(scoreMap_1);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/topscores")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(scoreMap_1));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.70").value("2021-03-30 10:33:50"))
                .andReturn();
    }

    @Test
    void testTopScoreBySinglePlayerForNonEmptyResponse_multipleTopScore() throws Exception {
        scoreList_1.add("2021-03-30 10:33:50");
        scoreList_1.add("2021-03-30 10:33:59");
        Integer topScore = 70;
        scoreMap_1.put(topScore, scoreList_1);
        when(mockPlayerDaoService.fetchTopScores(anyString())).thenReturn(scoreMap_1);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/topscores")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(scoreMap_1));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("{\"70\":[\"2021-03-30 10:33:50\",\"2021-03-30 10:33:59\"]}"))
                .andReturn();
    }

    @Test
    void testTopScoreBySinglePlayerForFailureCase_WhenPlayerNotFound() throws Exception {
        scoreList_1.add("2021-03-30 10:33:50");
        scoreList_1.add("2021-03-30 10:33:59");
        Integer topScore = 70;
        scoreMap_1.put(topScore, scoreList_1);
        when(mockPlayerDaoService.fetchTopScores(anyString())).thenThrow(PlayerNotFoundException.class);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/topscores")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(scoreMap_1));
        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    void testWorstScoreBySinglePlayerForNonEmptyResponse_singleLowestScore() throws Exception {
        scoreList_1.add("2021-03-30 10:33:50");
        Integer worstScore = 40;
        scoreMap_1.put(worstScore, scoreList_1);
        when(mockPlayerDaoService.fetchWorstScores(anyString())).thenReturn(scoreMap_1);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/lowestscores")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(scoreMap_1));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("{\"40\":[\"2021-03-30 10:33:50\"]}"))
                .andReturn();
    }

    @Test
    void testWorstScoreBySinglePlayerForNonEmptyResponse_multipleLowScore() throws Exception {
        scoreList_1.add("2021-03-30 10:33:50");
        scoreList_1.add("2021-03-30 10:33:59");
        Integer worstScore = 40;
        scoreMap_1.put(worstScore, scoreList_1);
        when(mockPlayerDaoService.fetchWorstScores(anyString())).thenReturn(scoreMap_1);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/lowestscores")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(scoreMap_1));

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("{\"40\":[\"2021-03-30 10:33:50\",\"2021-03-30 10:33:59\"]}"))
                .andReturn();
    }

    @Test
    void testWorstScoreBySinglePlayerForFailureCase_WhenPlayerNotFound() throws Exception {
        when(mockPlayerDaoService.fetchWorstScores(anyString())).thenThrow(PlayerNotFoundException.class);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/lowestscores")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(scoreMap_1));
        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    void testAverageScoreBySinglePlayerForNonEmptyResponse_sucessCase() throws Exception {
        when(mockPlayerDaoService.fetchAverageScore(anyString())).thenReturn(8.7);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/averagescore")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(8.7));
    }

    @Test
    void testAverageBySinglePlayerForNonEmptyResponse_Failure() throws Exception {
        when(mockPlayerDaoService.fetchAverageScore(anyString())).thenThrow(PlayerNotFoundException.class);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/averagescore")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    void testFetchAllScoresForSinglePlayer_NoEmptyResponseSucessCase() throws Exception {
        scoreList_1.add("2021-03-30 10:33:50");
        scoreList_1.add("2021-03-30 10:33:59");

        scoreList_2.add("2021-03-30 10:33:50");
        scoreList_2.add("2021-03-30 10:33:59");
        scoreList_2.add("2021-03-13 10:33:59");

        scoreList_3.add("2021-03-31 10:33:50");
        scoreList_3.add("2021-03-31 10:33:59");
        scoreList_3.add("2021-03-11 10:33:59");

        scoreMap_1.put(10, scoreList_1);
        scoreMap_1.put(40, scoreList_2);
        scoreMap_1.put(0, scoreList_2);

        when(mockPlayerDaoService.fetchAllScoresByPlayerName(anyString())).thenReturn(scoreMap_1);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/allScores")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(print())
                .andExpect(content().string("{\"0\":[\"2021-03-30 10:33:50\",\"2021-03-30 10:33:59\",\"2021-03-13 10:33:59\"],\"40\":[\"2021-03-30 10:33:50\",\"2021-03-30 10:33:59\",\"2021-03-13 10:33:59\"],\"10\":[\"2021-03-30 10:33:50\",\"2021-03-30 10:33:59\"]}"))
                .andReturn();
    }

    @Test
    void testFetchAllScoresForSinglePlayer_ErrorCase() throws Exception {
        when(mockPlayerDaoService.fetchAllScoresByPlayerName(anyString())).thenThrow(PlayerNotFoundException.class);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/dot/com/v1/players/virat/allScores")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }
}