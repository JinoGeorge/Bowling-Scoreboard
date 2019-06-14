package de.arena.bowling.service;

import de.arena.bowling.domain.SinglePlayerGame;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoDB repository to store all the game and scoreboards.
 * It uses Spring data mongoDB.
 */
interface ScoreboardRepository extends MongoRepository<SinglePlayerGame, String> {
}
