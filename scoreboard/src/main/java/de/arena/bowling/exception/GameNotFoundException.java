package de.arena.bowling.exception;

public class GameNotFoundException extends GenericScoreboardException {

    public GameNotFoundException(String gameId) {
        super(String.format("Could not find a matching game with Id: %s", gameId));
    }
}
