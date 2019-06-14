package de.arena.bowling.exception;

public class AttemptToUpdateCompletedGameException extends GenericScoreboardException {

    public AttemptToUpdateCompletedGameException(String gameId) {
        super(String.format("Can not update score of a completed game: %s", gameId));
    }
}
