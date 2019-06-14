package de.arena.bowling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for the spring boot context load and start up.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ScoreboardApplicationTests {

	@Test
	@DisplayName("Test if the whole application boots up correctly")
	void contextLoads() {
		assertTrue(true);
	}

}
