package de.arena.bowling.service;

import de.arena.bowling.domain.SinglePlayerGame;
import de.arena.bowling.exception.GenericScoreboardException;

import java.util.Optional;

/**
 * Service methods for the bowling scoreboard.
 */
public interface ScoreboardService {

    /**
     * Creates a new {@link SinglePlayerGame} and initializes the scoreboard.
     *
     * @return new {@link SinglePlayerGame} instance with a new {@link de.arena.bowling.domain.Scoreboard}
     */
    SinglePlayerGame createNewGameScoreboard();

    /**
     * Fetches the scoreboard with the specified 'gameId'.
     *
     * @param gameId the unique id of the game used to persist the scoreboard.
     * @return Optional.empty() if gameId is null or no match found.
     */
    Optional<SinglePlayerGame> fetchScoreboard(String gameId);

    /**
     * Updates the scoreboard with the new number of pins hit in the last roll.
     *
     * @param game         the {@link SinglePlayerGame} to be updated with the new number of pins hit.
     * @param numberOfPins number of pins hit in the last roll.
     * @throws GenericScoreboardException in case of invalid input parameters.
     */
    void updateScore(SinglePlayerGame game, int numberOfPins) throws GenericScoreboardException;
}
