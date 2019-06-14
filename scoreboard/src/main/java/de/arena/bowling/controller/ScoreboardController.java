package de.arena.bowling.controller;

import de.arena.bowling.domain.GameStatus;
import de.arena.bowling.domain.Scoreboard;
import de.arena.bowling.domain.SinglePlayerGame;
import de.arena.bowling.exception.GameNotFoundException;
import de.arena.bowling.exception.GenericScoreboardException;
import de.arena.bowling.service.ScoreboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Controller to handle the requests to the bowling scoreboard view.
 */
@Controller
@Validated
@Slf4j
public class ScoreboardController {

    private ScoreboardService scoreboardService;

    @Autowired
    public ScoreboardController(ScoreboardService scoreboardService) {
        this.scoreboardService = scoreboardService;
    }

    /**
     * Handles the root level path and redirects to the "/start" page handler.
     */
    @GetMapping("/")
    public String redirectToStartPage() {
        log.info("Redirecting request to /start");
        return "redirect:start";
    }

    /**
     * Initializes a new game scoreboard and return to the start view.
     */
    @GetMapping({"new-game", "start"})
    public String createNewGame(Model model) {
        log.info("Initializing a new game scoreboard.");
        SinglePlayerGame newGame = scoreboardService.createNewGameScoreboard();
        model.addAttribute("game", newGame);
        model.addAttribute("frames", newGame.getScoreboard().getFrames());
        return "scoreboard";
    }

    /**
     * Updates the scoreboard of the specified game with the number of pins hit.
     *
     * @param gameId       the unique id of the game to be updated.
     * @param numberOfPins number pins hit
     * @throws GameNotFoundException if no game is found with the specified game id.
     */
    @GetMapping("score")
    public String score(@RequestParam @NotBlank String gameId,
                        @RequestParam @NotNull Integer numberOfPins,
                        Model model) throws GameNotFoundException {
        log.info("Updating scoreboard of game {}", gameId);
        SinglePlayerGame game = scoreboardService.fetchScoreboard(gameId)
                .orElseThrow(() -> {
                    log.error("Could not find game with id {}", gameId);
                    return new GameNotFoundException(gameId);
                });
        try {
            scoreboardService.updateScore(game, numberOfPins);
        } catch (GenericScoreboardException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        model.addAttribute("game", game);
        model.addAttribute("frames", game.getScoreboard().getFrames());
        if (GameStatus.GAME_OVER == game.getScoreboard().getStatus()) {
            model.addAttribute("gameStatus", getGameStatusMessage(game.getScoreboard()));
        }
        return "scoreboard";
    }

    /**
     * Gets the description of the final score.
     * Its a 'Perfect Game' if the total score is 300;
     */
    private String getGameStatusMessage(Scoreboard scoreboard) {
        return scoreboard.isPerfectGame() ? "Bravo! that was a 'Perfect Game'" :
                "Game over, you scored " + scoreboard.getTotalScore() + " points";
    }
}
