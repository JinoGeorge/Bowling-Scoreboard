package de.arena.bowling.service;

import de.arena.bowling.domain.GameStatus;
import de.arena.bowling.domain.Scoreboard;
import de.arena.bowling.domain.SinglePlayerGame;
import de.arena.bowling.exception.GenericScoreboardException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link ScoreboardServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
class ScoreboardServiceImplTest {

    @Mock
    private ScoreboardRepository repository;

    @InjectMocks
    private ScoreboardServiceImpl scoreboardService;

    @Test
    @DisplayName("Test successful creation of a new game scoreboard")
    void createNewGameScoreboard() {
        //when
        when(repository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        SinglePlayerGame game = scoreboardService.createNewGameScoreboard();
        //then
        assertThat(game.getScoreboard()).isNotNull();
        assertThat(game.getScoreboard().getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(game.getScoreboard().getFrames()).isNotNull();
    }

    @Test
    @DisplayName("Test the fetch operation with null game id")
    void fetchScoreboardWithNullGameId() {
        assertThat(scoreboardService.fetchScoreboard(null)).isEmpty();
    }

    @Test
    @DisplayName("Test successful fetching of a game using game id")
    void fetchScoreboard() {
        //when
        String gameId = "gameId";
        SinglePlayerGame singlePlayerGame = SinglePlayerGame.builder().gameId(gameId).build();
        when(repository.findById(gameId)).thenReturn(Optional.of(singlePlayerGame));
        //then
        assertThat(scoreboardService.fetchScoreboard(gameId)).hasValue(singlePlayerGame);
    }

    @Test
    @DisplayName("Test score calculation set 1")
    void scoreCalculationTest1() throws GenericScoreboardException {
        SinglePlayerGame game = SinglePlayerGame.builder()
                .scoreboard(
                        Scoreboard.builder()
                                .frames(new LinkedList<>())
                                .status(GameStatus.IN_PROGRESS)
                                .build())
                .build();

        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 5);
        scoreboardService.updateScore(game, 5);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 7);
        scoreboardService.updateScore(game, 3);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 5);
        scoreboardService.updateScore(game, 5);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 7);
        scoreboardService.updateScore(game, 3);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 5);

        //then
        assertThat(game.getScoreboard().getStatus()).isEqualTo(GameStatus.GAME_OVER);
        assertThat(game.getScoreboard().getTotalScore()).isEqualTo(210);
        assertThat(game.getScoreboard().isPerfectGame()).isFalse();
    }

    @Test
    @DisplayName("Test score calculation set 2")
    void scoreCalculationTest2() throws GenericScoreboardException {
        SinglePlayerGame game = SinglePlayerGame.builder()
                .scoreboard(
                        Scoreboard.builder()
                                .frames(new LinkedList<>())
                                .status(GameStatus.IN_PROGRESS)
                                .build())
                .build();

        scoreboardService.updateScore(game, 3);
        scoreboardService.updateScore(game, 5);
        scoreboardService.updateScore(game, 4);
        scoreboardService.updateScore(game, 2);
        scoreboardService.updateScore(game, 6);
        scoreboardService.updateScore(game, 4);
        scoreboardService.updateScore(game, 2);
        scoreboardService.updateScore(game, 6);
        scoreboardService.updateScore(game, 2);
        scoreboardService.updateScore(game, 7);
        scoreboardService.updateScore(game, 4);
        scoreboardService.updateScore(game, 1);
        scoreboardService.updateScore(game, 3);
        scoreboardService.updateScore(game, 6);
        scoreboardService.updateScore(game, 1);
        scoreboardService.updateScore(game, 6);
        scoreboardService.updateScore(game, 2);
        scoreboardService.updateScore(game, 5);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        //then
        assertThat(game.getScoreboard().getStatus()).isEqualTo(GameStatus.GAME_OVER);
        assertThat(game.getScoreboard().getTotalScore()).isEqualTo(101);
        assertThat(game.getScoreboard().isPerfectGame()).isFalse();
    }

    @Test
    @DisplayName("Test score calculation set 3")
    void scoreCalculationTest3() throws GenericScoreboardException {
        SinglePlayerGame game = SinglePlayerGame.builder()
                .scoreboard(
                        Scoreboard.builder()
                                .frames(new LinkedList<>())
                                .status(GameStatus.IN_PROGRESS)
                                .build())
                .build();

        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        scoreboardService.updateScore(game, 10);
        //then
        assertThat(game.getScoreboard().getStatus()).isEqualTo(GameStatus.GAME_OVER);
        assertThat(game.getScoreboard().getTotalScore()).isEqualTo(300);
        assertThat(game.getScoreboard().isPerfectGame()).isTrue();
    }
}
