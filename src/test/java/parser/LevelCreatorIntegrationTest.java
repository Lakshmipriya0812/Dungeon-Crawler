package parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

public class LevelCreatorIntegrationTest extends LevelCreatorITHelper {

	@Test // Rule: Player is an empty space with a player in it
	public void create_level() throws Throwable {
		List<String> levelStrings = createSimpleLevel();
		writeLevelFile(levelStrings);
		createLevel();

		playerIsLocatedAt(2, 1);
		levelStrings.set(1, "X  X");
		checkAllTiles(levelStrings);
	}

	@Test
	public void invalid_level() throws Throwable {
		List<String> levelStrings = createSimpleLevel();
		levelStrings.set(1, "X&PX");
		writeLevelFile(levelStrings);
		createLevel();

		assertFalse(exceptionMessage.isEmpty());
	}

	@Test
	public void malfunctioning_file_reader() throws Throwable {
		createSimpleLevel();
		createLevelWithMalfunctioningFileReader();
		assertThat(true, equalTo(gameEngine.isExit()));
	}
}
