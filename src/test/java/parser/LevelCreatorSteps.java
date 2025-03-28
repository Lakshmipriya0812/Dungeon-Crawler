package parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.mockito.Mockito;

import engine.GameEngine;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import tiles.TileType;
import timer.PowerUpTimer;
import values.TestingTunableParameters;
import wrappers.ReaderWrapper;
import wrappers.SystemWrapper;

public class LevelCreatorSteps extends LevelCreationStepDefHelper {
	private static final int ONE = 1;
	private static final int COORDINATE_OFFSET = ONE;
	ReaderWrapper readerWrapper;
	IOException ioException;
	private GameEngine gameEngine;
	private String exceptionMessage;

	@Given("^level is:$")
	public void level_is(List<String> levelStrings) throws Throwable {
		writeLevelFile(levelStrings);
	}

	@When("^I create the level$")
	public void i_create_the_level() throws Throwable {
		LevelCreator levelCreator = new LevelCreator(TestingTunableParameters.FILE_LOCATION_PREFIX,
				new ReaderWrapper());
		SystemWrapper systemWrapper = new SystemWrapper();
		PowerUpTimer powerUpTimer = new PowerUpTimer(systemWrapper);
		try {
			gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		} catch (IllegalArgumentException e) {
			exceptionMessage = e.getMessage();
		}
	}

	@When("^I create the level with malfunctioning reader$")
	public void i_create_the_level_with_malfunctioning_reader() throws Throwable {
		ioException = Mockito.mock(IOException.class);
		readerWrapper = Mockito.mock(ReaderWrapper.class);
		BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
		Mockito.when(readerWrapper.createBufferedReader(Mockito.anyString())).thenReturn(bufferedReader);
		Mockito.doThrow(ioException).when(bufferedReader).readLine();
		LevelCreator levelCreator = new LevelCreator(TestingTunableParameters.FILE_LOCATION_PREFIX, readerWrapper);
		SystemWrapper systemWrapper = new SystemWrapper();
		PowerUpTimer powerUpTimer = new PowerUpTimer(systemWrapper);
		gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
	}

	@Then("^starting from the top-left:$")
	public void starting_from_the_top_left() throws Throwable {
	}

	@Then("^the player's x coordinate is (\\d+)$")
	public void player_x_is(int playerX) throws Throwable {
		assertThat(gameEngine.getPlayerXCoordinate(), equalTo(playerX - COORDINATE_OFFSET));
	}

	@Then("^the player's y coordinate is (\\d+)$")
	public void player_y_is(int playerY) throws Throwable {
		assertThat(gameEngine.getPlayerYCoordinate(), equalTo(playerY - COORDINATE_OFFSET));
	}

	@Then("^\\((\\d+), (\\d+)\\) is \"([^\"]*)\"$")
	public void is(int x, int y, String tileChar) throws Throwable {
		char ch = tileChar.charAt(0);
		TileType actualTileType = gameEngine.getTileFromCoordinates(x - COORDINATE_OFFSET, y - COORDINATE_OFFSET);
		assertThat(actualTileType, equalTo(TileType.getTileTypeByChar(ch)));
	}

	@Then("^the invalid character error message is displayed$")
	public void the_invalid_character_error_message_is_displayed() throws Throwable {
		assertFalse(exceptionMessage.isEmpty());
	}

	@Then("^the message is: \"([^\"]*)\"$")
	public void the_message_is(String errorMessage) throws Throwable {
		assertThat(exceptionMessage, equalTo(errorMessage));
	}

	@Then("^the malfunctioning reader error message is displayed$")
	public void the_malfunctioning_reader_error_message_is_displayed() throws Throwable {
		assertThat(true, equalTo(gameEngine.isExit()));
	}
}