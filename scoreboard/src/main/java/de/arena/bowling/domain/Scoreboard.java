package de.arena.bowling.domain;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Bowling scoreboard with 10 frames.
 */
@Data
@Builder
public class Scoreboard {
    private GameStatus status;
    private LinkedList<Frame> frames;
    private Integer totalScore;

    /**
     * @return true if the total score of the game is 300
     */
    public boolean isPerfectGame() {
        return GameStatus.GAME_OVER == status && Objects.equals(totalScore, 300);
    }
}
