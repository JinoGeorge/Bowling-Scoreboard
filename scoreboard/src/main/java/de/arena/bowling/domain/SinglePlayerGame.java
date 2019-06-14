package de.arena.bowling.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A wrapper class to hold the game scoreboard and game id.
 * The primary key (gameId) will be generated while persisting the object.
 * It holds Scoreboard for a single player only.
 */
@Data
@Builder
@Document
public class SinglePlayerGame {
    @Id
    private String gameId;
    private Scoreboard scoreboard;
}


