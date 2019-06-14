package de.arena.bowling.domain;

import lombok.Data;

import java.util.Objects;

/**
 * Frame that holds two rolls and the bonus information.
 */
@Data
public class Frame {
    private static final String STRIKE = "X";
    private static final String SPARE = "/";
    private static final String MISS = "-";

    private final Integer frameCount;

    private Integer firstRoll;
    private Integer secondRoll;
    private Integer bonus;
    private Integer score;

    public boolean isRollsCompleted() {
        if (isFinalFrame()) {
            if (isStrike() || isSpare()) {
                return bonus != null;
            }
            return secondRoll != null;
        } else if (isStrike()) {
            return true;
        }
        return secondRoll != null;
    }

    public boolean isStrike() {
        return Objects.equals(firstRoll, 10);
    }

    public boolean isSpare() {
        if (!isStrike() && firstRoll != null && secondRoll != null) {
            return firstRoll + secondRoll == 10;
        }
        return false;
    }

    public boolean isFinalFrame() {
        return Objects.equals(frameCount, 10);
    }

    public boolean hasBonus() {
        return isStrike() || isSpare();
    }

    public String firstRollDisplayValue() {
        if (isStrike()) {
            return STRIKE;
        } else if (Objects.equals(firstRoll, 0)) {
            return MISS;
        } else {
            return firstRoll == null ? "" : firstRoll.toString();
        }
    }

    public String secondRollDisplayValue() {
        if (isSpare()) {
            return SPARE;
        } else if (Objects.equals(secondRoll, 0)) {
            return MISS;
        } else if (isFinalFrame() && Objects.equals(secondRoll, 10)) {
            return STRIKE;
        } else {
            return secondRoll == null ? "" : secondRoll.toString();
        }
    }

    public String bonusDisplayValue() {
        if (isFinalFrame() && hasBonus()) {
            if (Objects.equals(bonus, 10)) {
                return STRIKE;
            } else if (Objects.equals(bonus, 0)) {
                return MISS;
            } else {
                return bonus == null ? "" : bonus.toString();
            }
        }
        return "";
    }
}
