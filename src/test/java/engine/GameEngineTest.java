package engine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import parser.LevelCreator;
import tiles.TileType;
import timer.PowerUpTimer;
import ui.GameFrame;
import wrappers.SystemWrapper;

public class GameEngineTest {

	private static final int ZERO = 0;
	private static final int ONE = 1;
	@InjectMocks
	private GameEngine gameEngine;
	@Mock
	private LevelCreator levelCreator;
	private SystemWrapper systemWrapper;
	private PowerUpTimer powerUpTimer;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		systemWrapper = mock(SystemWrapper.class);
		powerUpTimer = mock(PowerUpTimer.class);
		when(systemWrapper.currentTimeMillis()).thenReturn(1000L);
		gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		int level = 1;
		Mockito.verify(levelCreator, Mockito.times(level)).createLevel(gameEngine, level);
	}

	@Test
	public void run() {
		GameFrame gameFrame = mock(GameFrame.class);
		Component component = mock(Component.class);
		systemWrapper = mock(SystemWrapper.class);
		powerUpTimer = mock(PowerUpTimer.class);
		gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		gameEngine.setPlayer(5, 5);
		when(gameFrame.getComponents()).thenReturn(new Component[] { component });
		gameEngine.run(gameFrame);
		Mockito.verify(component, Mockito.times(1)).repaint();
	}

	@Test
	public void add_and_get_tile() {
		TileType tileType = TileType.PASSABLE;
		gameEngine.addTile(ZERO, ONE, TileType.PASSABLE);
		TileType actual = gameEngine.getTileFromCoordinates(ZERO, ONE);
		assertThat(actual, equalTo(tileType));
	}

	@Test
	public void set_and_get_horizontal_dimension() {
		gameEngine.setLevelHorizontalDimension(ONE);
		int actual = gameEngine.getLevelHorizontalDimension();
		assertThat(actual, equalTo(ONE));
	}

	@Test
	public void set_and_get_vertical_dimension() {
		gameEngine.setLevelVerticalDimension(ONE);
		int actual = gameEngine.getLevelVerticalDimension();
		assertThat(actual, equalTo(ONE));
	}

	@Test
	public void add_and_get_player_coordinates() {
		TileType tileType = TileType.PLAYER;
		gameEngine.addTile(ZERO, ONE, tileType);
		int actualX = gameEngine.getPlayerXCoordinate();
		int actualY = gameEngine.getPlayerYCoordinate();
		assertThat(actualX, equalTo(ZERO));
		assertThat(actualY, equalTo(ONE));

	}

	@Test
	public void set_and_get_exit() {
		boolean exit = true;
		gameEngine.setExit(exit);
		boolean actual = gameEngine.isExit();
		assertThat(actual, equalTo(exit));
	}

	@Test

	public void player_moves_to_passable_tile_updates_player_position() {
		gameEngine.addTile(ZERO, ZERO, TileType.PLAYER);
		gameEngine.addTile(ONE, ZERO, TileType.PASSABLE);
		gameEngine.playerMoves(ONE, ZERO);
		int actualX = gameEngine.getPlayerXCoordinate();
		int actualY = gameEngine.getPlayerYCoordinate();
		assertThat(actualX, equalTo(ONE));
		assertThat(actualY, equalTo(ZERO));
	}

	@Test
	public void player_moves_to_not_passable_tile_does_not_update_player_position() {
		gameEngine.addTile(ZERO, ZERO, TileType.PLAYER);
		gameEngine.addTile(ONE, ZERO, TileType.NOT_PASSABLE);
		gameEngine.playerMoves(ONE, ZERO);
		int actualX = gameEngine.getPlayerXCoordinate();
		int actualY = gameEngine.getPlayerYCoordinate();
		assertThat(actualX, equalTo(ZERO));
		assertThat(actualY, equalTo(ZERO));
	}

	@Test
	public void is_exit_returns_correct_exit_state() {
		assertThat(gameEngine.isExit(), equalTo(false));
		gameEngine.setExit(true);
		assertThat(gameEngine.isExit(), equalTo(true));
	}

	@Test
	public void exception_if_coin_already_exists_at_position() {
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		gameEngine.addCoin(0, 0);
		assertThrows(IllegalArgumentException.class, () -> gameEngine.addCoin(0, 0),
				"Coin already exists at this position.");
	}

	@Test
	public void exception_if_coin_on_non_passable_tile() {
		gameEngine.addTile(0, 0, TileType.NOT_PASSABLE);
		assertThrows(IllegalArgumentException.class, () -> gameEngine.addCoin(0, 0),
				"Cannot place coin on a non-passable tile");
	}

	@Test
	public void is_coin_at_position_method_should_return_true_if_coin_exists() {
		gameEngine.addTile(2, 3, TileType.PASSABLE);
		gameEngine.addCoin(2, 3);
		boolean coinAtPosition = gameEngine.isCoinAtPosition(2, 3);
		assertThat(coinAtPosition, equalTo(true));
	}

	@Test
	public void is_coin_at_position_method_should_return_false_if_coin_not_exists() {
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		boolean coinAtPosition = gameEngine.isCoinAtPosition(0, 0);
		assertThat(coinAtPosition, equalTo(false));
	}

	@Test
	public void has_enough_passable_tiles_should_return_true_when_enough_passable_tiles() {
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addTile(0, 1, TileType.PASSABLE);
		gameEngine.addTile(1, 1, TileType.PASSABLE);
		gameEngine.setLevelHorizontalDimension(2);
		gameEngine.setLevelVerticalDimension(2);
		boolean result = gameEngine.hasEnoughPassableTiles(2);
		assertThat(result, equalTo(true));
	}

	@Test
	public void has_enough_passable_tiles_should_return_false_when_not_enough_passable_tiles() {
		gameEngine.addTile(0, 0, TileType.NOT_PASSABLE);
		boolean result = gameEngine.hasEnoughPassableTiles(2);
		assertThat(result, equalTo(false));
	}

	@Test
	public void add_random_coins_should_add_coins_to_passable_tiles() {
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addTile(0, 1, TileType.PASSABLE);
		gameEngine.addTile(1, 1, TileType.PASSABLE);
		gameEngine.setLevelHorizontalDimension(2);
		gameEngine.setLevelVerticalDimension(2);
		gameEngine.addRandomCoins(2);
		assertThat(gameEngine.getCoins().size(), equalTo(2));
	}

	@Test
	public void add_random_coins_should_not_add_coins_if_not_enough_passable_tiles() {
		gameEngine.addTile(0, 0, TileType.NOT_PASSABLE);
		gameEngine.addRandomCoins(2);
		assertThat(gameEngine.getCoins().size(), equalTo(0));
	}

	@Test
	public void collect_coin() {
		gameEngine.addTile(1, 1, TileType.PASSABLE);
		gameEngine.addCoin(1, 1);
		assertThat(gameEngine.getCoins().size(), equalTo(1));

		gameEngine.collectItem(1, 1, "Coin");

		assertThat(gameEngine.getCoins().size(), equalTo(0));
		assertThat(gameEngine.getCollectedCoins(), equalTo(1));
		assertThat(gameEngine.getTileFromCoordinates(1, 1), equalTo(TileType.PASSABLE));
	}

	@Test
	public void collect_coin_does_nothing_if_no_coin_at_position() {
		gameEngine.addTile(1, 1, TileType.PASSABLE);
		gameEngine.collectItem(1, 1, "Coin");
		assertThat(gameEngine.getCollectedCoins(), equalTo(0));
		assertThat(gameEngine.getTileFromCoordinates(1, 1), equalTo(TileType.PASSABLE));
	}

	@Test
	public void player_moves_collects_coin_on_coin_tile() {
		gameEngine.addTile(0, 0, TileType.PLAYER);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addCoin(1, 0);
		gameEngine.playerMoves(1, 0);

		assertThat(gameEngine.getPlayerXCoordinate(), equalTo(1));
		assertThat(gameEngine.getPlayerYCoordinate(), equalTo(0));
		assertThat(gameEngine.getCollectedCoins(), equalTo(1));
		assertThat(gameEngine.isCoinAtPosition(1, 0), equalTo(false));
	}

	@Test
	public void collected_coins_after_multiple_collects() {
		gameEngine.addTile(0, 0, TileType.PLAYER);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addTile(2, 0, TileType.PASSABLE);
		gameEngine.addCoin(1, 0);
		gameEngine.addCoin(2, 0);
		gameEngine.collectItem(1, 0, "Coin");
		gameEngine.collectItem(2, 0, "Coin");
		assertThat(gameEngine.isCoinAtPosition(1, 0), equalTo(false));
		assertThat(gameEngine.isCoinAtPosition(2, 0), equalTo(false));
	}

	@Test
	public void check_level_completion_exit_is_true_and_print_message() {
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addCoin(0, 0);
		gameEngine.addCoin(1, 0);
		gameEngine.collectItem(0, 0, "Coin");
		gameEngine.collectItem(1, 0, "Coin");
		PrintStream originalSystemOut = System.out;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream mockSystemOut = new PrintStream(baos);
		System.setOut(mockSystemOut);
		gameEngine.handleLevelCompletion();
		assertThat(gameEngine.isExit(), equalTo(true));
		String output = baos.toString().trim();
		assertThat(output, containsString("Level Completed!"));
		assertThat(output, containsString("Game Over! All coins collected."));
		System.setOut(originalSystemOut);
	}

	@Test
	public void check_level_not_completion_exit_is_false_and_print_message() {
		LevelCreator levelCreator = mock(LevelCreator.class);
		systemWrapper = mock(SystemWrapper.class);
		powerUpTimer = mock(PowerUpTimer.class);
		gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addCoin(0, 0);
		gameEngine.addCoin(1, 0);
		PrintStream originalSystemOut = System.out;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream mockSystemOut = new PrintStream(baos);
		System.setOut(mockSystemOut);
		gameEngine.collectItem(0, 0, "Coin");
		System.out.println("After collecting first coin, collected coins: " + gameEngine.getCollectedCoins());
		gameEngine.collectItem(0, 0, "Coin");
		System.out.println(
				"After trying to collect first coin again, collected coins: " + gameEngine.getCollectedCoins());
		String output = baos.toString().trim();
		System.out.println("Captured output: " + output);
		assertThat(output, containsString("Level not Completed"));
		assertThat(gameEngine.isExit(), equalTo(false));
		System.setOut(originalSystemOut);
	}

	@Test
	public void check_method_enemy_initialization() {
		gameEngine.setLevelVerticalDimension(10);
		gameEngine.initializeEnemy(8, 7);
		Point enemyPosition = new Point(8, 7);
		assertEquals(TileType.ENEMY, gameEngine.getTileFromCoordinates(enemyPosition.x, enemyPosition.y),
				"Enemy should be at the expected position.");
	}

	@Test
	public void check_enemy_is_placed_on_tile_map() {
		gameEngine.setLevelHorizontalDimension(10);
		gameEngine.setLevelVerticalDimension(10);
		gameEngine.initializeEnemy(8, 7);
		Point enemyPosition = new Point(8, 7);
		assertEquals(TileType.ENEMY, gameEngine.getTileFromCoordinates(enemyPosition.x, enemyPosition.y),
				"Enemy tile should be placed at the correct position.");
	}

	@Test
	public void enemy_placed_at_different_positions() {
		gameEngine.setLevelHorizontalDimension(10);
		gameEngine.setLevelVerticalDimension(10);
		gameEngine.initializeEnemy(0, 0);
		Point enemyPosition = new Point(0, 0);
		assertEquals(TileType.ENEMY, gameEngine.getTileFromCoordinates(enemyPosition.x, enemyPosition.y),
				"Enemy should be at the top-left corner (0, 0).");
		gameEngine.initializeEnemy(9, 9);
		Point enemyPositionBottomRight = new Point(9, 9);
		assertEquals(TileType.ENEMY,
				gameEngine.getTileFromCoordinates(enemyPositionBottomRight.x, enemyPositionBottomRight.y),
				"Enemy should be at the bottom-right corner (9, 9).");
	}

	@Test
	public void get_enemy_method() {
		gameEngine.setLevelVerticalDimension(10);
		gameEngine.initializeEnemy(8, 7);
		Enemy actualEnemy = gameEngine.getEnemy();
		assertThat(actualEnemy.getX(), equalTo(8));
		assertThat(actualEnemy.getY(), equalTo(7));
	}

	@Test
	public void no_collision_when_enemy_and_player_are_on_different_tiles() {
		levelCreator = mock(LevelCreator.class);
		systemWrapper = mock(SystemWrapper.class);
		powerUpTimer = mock(PowerUpTimer.class);
		gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		gameEngine.setPlayer(5, 5);
		gameEngine.initializeEnemy(1, 1);
		gameEngine.checkForEnemyPlayerCollision();
		assertThat(gameEngine.isExit(), equalTo(false));
	}

	@Test
	public void player_moves_over_power_up_tile() {
		gameEngine.addTile(0, 0, TileType.PLAYER);
		gameEngine.addTile(1, 0, TileType.POWER_UP);
		gameEngine.playerMoves(1, 0);
		assertThat(gameEngine.getPlayerXCoordinate(), equalTo(1));
		assertThat(gameEngine.getPlayerYCoordinate(), equalTo(0));
	}

	@Test
	public void add_power_up_on_passable_tile() {
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		gameEngine.addPowerUp(0, 0);

		assertThat(gameEngine.getTileFromCoordinates(0, 0), equalTo(TileType.POWER_UP));
	}

	@Test
	public void cannot_add_power_up_on_coin_tile() {
		gameEngine.addTile(1, 1, TileType.COIN);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> gameEngine.addPowerUp(1, 1));
		assertThat(exception.getMessage(), containsString("Cannot place PowerUp on a coin tile."));
	}

	@Test
	public void cannot_add_power_up_on_non_passable_tile() {
		gameEngine.addTile(2, 2, TileType.NOT_PASSABLE);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> gameEngine.addPowerUp(2, 2));
		assertThat(exception.getMessage(), containsString("Cannot place PowerUp on a non-passable tile."));
	}

	@Test
	public void cannot_add_power_up_on_existing_power_up_tile() {
		gameEngine.addTile(3, 3, TileType.PASSABLE);
		gameEngine.addPowerUp(3, 3);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> gameEngine.addPowerUp(3, 3));
		assertThat(exception.getMessage(), containsString("PowerUp already exists at this position."));
	}

	@Test
	public void add_random_power_ups_is_called_properly() {
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.setLevelHorizontalDimension(2);
		gameEngine.setLevelVerticalDimension(1);
		gameEngine.addRandomPowerUps(2);
		assertThat(gameEngine.getPowerUps().size(), equalTo(2));
	}

	@Test
	public void add_random_power_ups_with_insufficient_passable_tiles() {
		gameEngine.addTile(0, 0, TileType.NOT_PASSABLE);
		gameEngine.addTile(1, 0, TileType.NOT_PASSABLE);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		gameEngine.addRandomPowerUps(2);
		String output = outputStream.toString().trim();
		assertThat(output, containsString("There are not enough passable tiles to place 2 PowerUp."));
		assertThat(gameEngine.getPowerUps().size(), equalTo(0));
		System.setOut(System.out);
	}

	@Test
	public void add_power_up_method_is_called_properly() {
		gameEngine.addTile(0, 0, TileType.PASSABLE);
		gameEngine.addPowerUp(0, 0);
		assertThat(gameEngine.getTileFromCoordinates(0, 0), equalTo(TileType.POWER_UP));
	}

	@Test
	void collects_power_up_tile() {
		gameEngine.addTile(1, 1, TileType.PASSABLE);
		gameEngine.addPowerUp(1, 1);
		assertThat(gameEngine.getPowerUps().size(), equalTo(1));

		gameEngine.collectItem(1, 1, "PowerUp");

		assertThat(gameEngine.getPowerUps().size(), equalTo(0));
		assertThat(gameEngine.getCollectedPowerUps(), equalTo(1));
		assertThat(gameEngine.getTileFromCoordinates(1, 1), equalTo(TileType.PASSABLE));
	}

	@Test
	void player_collects_power_up_tile() {
		gameEngine.addTile(0, 0, TileType.PLAYER);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addPowerUp(1, 0);
		gameEngine.playerMoves(1, 0);

		assertThat(gameEngine.getPlayerXCoordinate(), equalTo(1));
		assertThat(gameEngine.getPlayerYCoordinate(), equalTo(0));
		assertThat(gameEngine.getCollectedPowerUps(), equalTo(1));
		assertThat(gameEngine.isPowerUpAtPosition(1, 0), equalTo(false));
	}

	@Test
	void power_up_tile_removed_after_collection() {
		gameEngine.addTile(0, 0, TileType.PLAYER);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addPowerUp(1, 0);
		gameEngine.playerMoves(1, 0);
		assertThat(gameEngine.getPlayerXCoordinate(), equalTo(1));
		assertThat(gameEngine.getPlayerYCoordinate(), equalTo(0));
		assertThat(gameEngine.getCollectedPowerUps(), equalTo(1));
		assertThat(gameEngine.isPowerUpAtPosition(1, 0), equalTo(false));
	}

	@Test
	void power_up_cannot_be_placed_on_coin_tile() {
		levelCreator = mock(LevelCreator.class);
		systemWrapper = mock(SystemWrapper.class);
		powerUpTimer = mock(PowerUpTimer.class);
		gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		gameEngine.addTile(1, 0, TileType.PASSABLE);
		gameEngine.addCoin(1, 0);
		assertThat(gameEngine.isCoinAtPosition(1, 0), equalTo(true));
		Exception exception = assertThrows(IllegalArgumentException.class, () -> gameEngine.addPowerUp(1, 0));
		assertThat(exception.getMessage(), containsString("Cannot place PowerUp on a coin tile."));
		assertThat(gameEngine.isPowerUpAtPosition(1, 0), equalTo(false));
	}

	@Test
	void coin_cannot_be_placed_on_power_up_tile() {
		levelCreator = mock(LevelCreator.class);
		systemWrapper = mock(SystemWrapper.class);
		powerUpTimer = mock(PowerUpTimer.class);
		gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		gameEngine.addTile(2, 0, TileType.PASSABLE);
		gameEngine.addPowerUp(2, 0);
		assertThat(gameEngine.isPowerUpAtPosition(2, 0), equalTo(true));
		Exception exception = assertThrows(IllegalArgumentException.class, () -> gameEngine.addCoin(2, 0));
		assertThat(exception.getMessage(), containsString("Cannot place Coin on a power_up tile"));
		assertThat(gameEngine.isCoinAtPosition(2, 0), equalTo(false));
	}

	@Test
	public void has_enough_passable_tiles_including_coin_enemy_power_up_and_results_true() {
		gameEngine.setLevelHorizontalDimension(10);
		gameEngine.setLevelVerticalDimension(10);
		gameEngine.addTile(0, 2, TileType.NOT_PASSABLE);
		gameEngine.addTile(1, 2, TileType.NOT_PASSABLE);
		gameEngine.addTile(2, 2, TileType.PASSABLE);
		gameEngine.addTile(3, 2, TileType.PASSABLE);
		gameEngine.addTile(4, 2, TileType.PASSABLE);
		gameEngine.addTile(5, 2, TileType.PASSABLE);
		gameEngine.addTile(2, 2, TileType.COIN);
		gameEngine.addTile(3, 2, TileType.COIN);
		gameEngine.addTile(4, 2, TileType.POWER_UP);
		gameEngine.addTile(5, 2, TileType.ENEMY);
		assertFalse(gameEngine.hasEnoughPassableTiles(4));
	}

	@Test
	public void no_enough_passable_tiles_including_coin_enemy_power_up_and_results_false() {
		gameEngine.setLevelHorizontalDimension(10);
		gameEngine.setLevelVerticalDimension(10);
		gameEngine.addTile(0, 2, TileType.NOT_PASSABLE);
		gameEngine.addTile(1, 2, TileType.NOT_PASSABLE);
		gameEngine.addTile(3, 2, TileType.PASSABLE);
		gameEngine.addTile(4, 2, TileType.PASSABLE);
		gameEngine.addTile(5, 2, TileType.PASSABLE);
		gameEngine.addTile(3, 2, TileType.COIN);
		gameEngine.addTile(4, 2, TileType.POWER_UP);
		gameEngine.addTile(5, 2, TileType.ENEMY);
		assertFalse(gameEngine.hasEnoughPassableTiles(4));
	}

	@Test
	public void player_moves_to_enemy_and_triggers_exit() {
		levelCreator = mock(LevelCreator.class);
		systemWrapper = mock(SystemWrapper.class);
		powerUpTimer = mock(PowerUpTimer.class);
		gameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		gameEngine.setLevelHorizontalDimension(10);
		gameEngine.setLevelVerticalDimension(10);
		gameEngine.addTile(5, 5, TileType.PLAYER);
		gameEngine.addTile(5, 5, TileType.ENEMY);
		gameEngine.initializeEnemy(5, 5);
		gameEngine.checkForEnemyPlayerCollision();
		assertTrue(gameEngine.isExit());
	}

}
