package de.arena.bowling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starting point of the spring boot web application.
 * It loads the spring application context and initializes the web application.
 */
@SpringBootApplication
@Slf4j
public class ScoreboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoreboardApplication.class, args);
        log.info("Scoreboard application started successfull. \n You can navigate to localhost:8080/");
    }
}
