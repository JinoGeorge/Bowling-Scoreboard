package de.arena.bowling.exception;

public class InvalidNumberOfPinsException extends GenericScoreboardException {

    public InvalidNumberOfPinsException(int numberOfPins, int maxPossibleNumberOfPins) {
        super(String.format("Invalid number of pins; provided number: %d; highest possible number: %d",
                numberOfPins, maxPossibleNumberOfPins));
    }
}
