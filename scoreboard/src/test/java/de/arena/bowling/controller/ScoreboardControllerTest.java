package de.arena.bowling.controller;

import de.arena.bowling.domain.GameStatus;
import de.arena.bowling.domain.Scoreboard;
import de.arena.bowling.domain.SinglePlayerGame;
import de.arena.bowling.service.ScoreboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the {@link ScoreboardController} behaviours
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(ScoreboardController.class)
class ScoreboardControllerTest {

    @Autowired
    private MockMvc controller;

    @MockBean
    private ScoreboardService scoreboardService;

    @Test
    @DisplayName("Test if the root url is redirected to the start page")
    void redirectToStartPage() throws Exception {
        controller.perform(MockMvcRequestBuilders
                .get("/"))
                .andExpect(redirectedUrl("start"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Test successful creation of a new game scoreboard")
    void createNewGame() throws Exception {
        //given
        when(scoreboardService.createNewGameScoreboard())
                .thenReturn(SinglePlayerGame.builder()
                        .scoreboard(Scoreboard.builder()
                                .frames(new LinkedList<>())
                                .build())
                        .build());
        //when-then
        controller.perform(MockMvcRequestBuilders
                .get("/start"))
                .andExpect(model().attributeExists("game", "frames"))
                .andExpect(view().name("scoreboard"))
                .andExpect(status().isOk());
        verify(scoreboardService).createNewGameScoreboard();
    }

    @Test
    @DisplayName("Test score endpoint exception handling with invalid parameters")
    void scoreWithInvalidRequestParams() throws Exception {
        controller.perform(MockMvcRequestBuilders
                .get("/score").param("gameId", "").param("numberOfPins", ""))
                .andExpect(model().attributeExists("errorMessages"))
                .andExpect(view().name("error"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test successful score calculation")
    void score() throws Exception {
        // given
        String testGameId = "testId";
        when(scoreboardService.fetchScoreboard(testGameId))
                .thenReturn(Optional.of(SinglePlayerGame.builder()
                        .scoreboard(Scoreboard.builder()
                                .frames(new LinkedList<>())
                                .status(GameStatus.GAME_OVER)
                                .build())
                        .build()));
        //when-then
        controller.perform(MockMvcRequestBuilders
                .get("/score").param("gameId", testGameId).param("numberOfPins", "10"))
                .andExpect(model().attributeExists("game", "frames", "gameStatus"))
                .andExpect(view().name("scoreboard"))
                .andExpect(status().isOk());
        verify(scoreboardService).fetchScoreboard(testGameId);
    }
}
