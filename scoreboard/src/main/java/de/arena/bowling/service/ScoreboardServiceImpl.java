package de.arena.bowling.service;

import de.arena.bowling.domain.*;
import de.arena.bowling.exception.AttemptToUpdateCompletedGameException;
import de.arena.bowling.exception.GenericScoreboardException;
import de.arena.bowling.exception.InvalidNumberOfPinsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Implementation for the scoreboard service methods.
 */
@Service
@Slf4j
public class ScoreboardServiceImpl implements ScoreboardService {

    private ScoreboardRepository scoreboardRepository;

    @Autowired
    public ScoreboardServiceImpl(ScoreboardRepository scoreboardRepository) {
        this.scoreboardRepository = scoreboardRepository;
    }

    @Override
    public SinglePlayerGame createNewGameScoreboard() {
        log.info("Creating and persisting new SinglePlayerGame scoreboard");
        SinglePlayerGame game = SinglePlayerGame.builder()
                .scoreboard(Scoreboard.builder().frames(new LinkedList<>()).status(GameStatus.IN_PROGRESS).build())
                .build();
        return scoreboardRepository.save(game);
    }

    @Override
    public Optional<SinglePlayerGame> fetchScoreboard(String gameId) {
        return gameId == null ? Optional.empty() : scoreboardRepository.findById(gameId);
    }

    @Override
    public void updateScore(SinglePlayerGame game, int numberOfPins) throws GenericScoreboardException {
        if (game == null) {
            throw new IllegalArgumentException("Game can not be null");
        }
        Scoreboard scoreboard = game.getScoreboard();
        if (GameStatus.GAME_OVER == scoreboard.getStatus()) {
            throw new AttemptToUpdateCompletedGameException(game.getGameId());
        }

        int maxPossibleNumberOfPins = maxPossibleNumberOfPins(scoreboard);
        if (maxPossibleNumberOfPins < numberOfPins) {
            throw new InvalidNumberOfPinsException(numberOfPins, maxPossibleNumberOfPins);
        }

        log.info("Validated input parameters and scoreboard state; calculating latest score.");
        Frame activeFrame = getActiveFrame(scoreboard);
        updateFrameWithLastRoll(activeFrame, numberOfPins);
        calculateBonusForEachFrame(scoreboard.getFrames());
        calculateScore(scoreboard.getFrames());

        // update total score and game status
        if (activeFrame.getScore() != null) {
            scoreboard.setTotalScore(activeFrame.getScore());
        }
        if (activeFrame.isFinalFrame() && activeFrame.isRollsCompleted()) {
            scoreboard.setStatus(GameStatus.GAME_OVER);
        }
        scoreboardRepository.save(game);
    }

    /**
     * Returns the maximum possible number of pins in the next roll.
     */
    private int maxPossibleNumberOfPins(Scoreboard scoreboard) {
        if (GameStatus.GAME_OVER == scoreboard.getStatus()) {
            return 0;
        }

        Frame activeFrame = getActiveFrame(scoreboard);
        if (activeFrame.getFirstRoll() == null) {
            return 10;
        } else if (activeFrame.isFinalFrame()) {
            return activeFrame.isStrike() || activeFrame.hasBonus() ? 10 : 10 - activeFrame.getFirstRoll();
        } else {
            return 10 - activeFrame.getFirstRoll();
        }
    }

    /**
     * Gets the last frame to be updated.
     * If the scoreboard is empty or rolls completed for the last frame, a new frame is created.
     */
    private Frame getActiveFrame(Scoreboard scoreboard) {
        if (scoreboard.getFrames().isEmpty()) {
            scoreboard.getFrames().add(new Frame(1));
        }
        Frame activeFrame = scoreboard.getFrames().peekLast();
        if (activeFrame.isRollsCompleted() && GameStatus.IN_PROGRESS == scoreboard.getStatus()) {
            activeFrame = new Frame(activeFrame.getFrameCount() + 1);
            scoreboard.getFrames().addLast(activeFrame);
        }
        return activeFrame;
    }

    /**
     * Adds the number of pins into the active frame.
     *
     * @param frame        to be updated
     * @param numberOfPins to be added into the frame.
     */
    void updateFrameWithLastRoll(Frame frame, int numberOfPins) {
        if (frame.getFirstRoll() == null) {
            frame.setFirstRoll(numberOfPins);
        } else if (frame.getSecondRoll() == null) {
            frame.setSecondRoll(numberOfPins);
        } else if (frame.isFinalFrame() && frame.hasBonus()) {
            frame.setBonus(numberOfPins);
        }
    }

    /**
     * For a Strike or a Spare, the bonus points needs to be calculated before the total score in that frame.
     * For a Strike, you get points from the next two rolls.
     * For a Spare, you get points from the next'one roll.
     */
    void calculateBonusForEachFrame(Deque<Frame> boardFrames) {
        CircularQueue<Integer> succeedingRolls = new CircularQueue<>(2);
        Iterator<Frame> frameIterator = boardFrames.descendingIterator();

        while (frameIterator.hasNext()) {
            Frame currentFrame = frameIterator.next();
            if (currentFrame.isRollsCompleted() && currentFrame.getBonus() == null) {
                if (currentFrame.isStrike()) {
                    currentFrame.setBonus(getSumPointsFromTwoSucceedingRolls(succeedingRolls));
                } else if (currentFrame.isSpare()) {
                    currentFrame.setBonus(getPointsFromSucceedingRoll(succeedingRolls));
                } else if (currentFrame.getBonus() == null) {
                    currentFrame.setBonus(0);
                }
            }
            // adding the last two rolls into the circular queue
            if (currentFrame.getSecondRoll() != null) {
                succeedingRolls.addFirst(currentFrame.getSecondRoll());
            }
            if (currentFrame.getFirstRoll() != null) {
                succeedingRolls.addFirst(currentFrame.getFirstRoll());
            }
        }
    }

    /**
     * Once the bonus points are set, we can calculate the total score for the frame.
     */
    private void calculateScore(Deque<Frame> boardFrames) {
        // calculate total score for each frame
        Integer cumulativeScore = 0;
        for (Frame frame : boardFrames) {
            if (frame.getBonus() != null) {
                Integer frameTotal = frame.getFirstRoll() + frame.getBonus() + Optional.ofNullable(frame.getSecondRoll()).orElse(0);
                cumulativeScore += frameTotal;
                frame.setScore(cumulativeScore);
            }
        }
    }

    /**
     * Gets the points from one succeeding roll.
     */
    private Integer getPointsFromSucceedingRoll(CircularQueue<Integer> rollsInFront) {
        if (!rollsInFront.isEmpty()) {
            return rollsInFront.get(0);
        }
        return null;
    }

    /**
     * Gets the sum of points from the two succeeding rolls.
     */
    private Integer getSumPointsFromTwoSucceedingRolls(CircularQueue<Integer> rollsInFront) {
        if (rollsInFront.size() == 2) {
            return rollsInFront.get(0) + rollsInFront.get(1);
        }
        return null;
    }
}
