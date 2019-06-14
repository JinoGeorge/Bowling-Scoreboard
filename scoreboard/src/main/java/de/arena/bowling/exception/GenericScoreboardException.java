package de.arena.bowling.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericScoreboardException extends Exception {

    public GenericScoreboardException(String message) {
        super(message);
    }
}
