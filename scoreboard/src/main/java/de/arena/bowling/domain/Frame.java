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

    /**
     * Number of pins hit in the first roll
     */
    private Integer firstRoll;
    /**
     * Number of pins hit in the second roll
     */
    private Integer secondRoll;
    /**
     * Bonus points if exists
     */
    private Integer bonus;
    /**
     * Total score up to this frame (cumulative score)
     */
    private Integer score;

    /**
     * Indicates if the 2 rolls are completed for this frame.
     * For the final frame, depends on the strike or spare a bonus roll is given.
     * @return true, if all the rolls are completed for this frame.
     */
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

    /**
     * @return true if the frame got a strike.
     * ie. 10 pins hit in the first roll.
     */
    public boolean isStrike() {
        return Objects.equals(firstRoll, 10);
    }

    /**
     * @return true if the frame got a spare.
     * ie. 10 pins from two rolls together.
     */
    public boolean isSpare() {
        if (!isStrike() && firstRoll != null && secondRoll != null) {
            return firstRoll + secondRoll == 10;
        }
        return false;
    }

    /**
     * @return true, if this is the 10th frame in the game.
     */
    public boolean isFinalFrame() {
        return Objects.equals(frameCount, 10);
    }

    /**
     * @return true, if there is bonus for this frame from Strike or Spare.
     */
    public boolean hasBonus() {
        return isStrike() || isSpare();
    }

    /**
     * Display value for the first roll.
     * Strike -> X
     * Miss -> -
     * Otherwise -> number of hits
     * @return the display value for the first roll.
     */
    public String firstRollDisplayValue() {
        if (isStrike()) {
            return STRIKE;
        } else if (Objects.equals(firstRoll, 0)) {
            return MISS;
        } else {
            return firstRoll == null ? "" : firstRoll.toString();
        }
    }

    /**
     * Display value for the second roll.
     * Strike -> X
     * Spare -> /
     * Miss -> -
     * Otherwise -> number of hits
     * @return the display value for the second roll.
     */
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

    /**
     * Display value for the bonus roll in the 10th frame.
     * Strike -> X
     * Miss -> -
     * Otherwise -> number of hits
     * @return the display value for the second roll.
     */
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
