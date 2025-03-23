package ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import engine.Coin;
import engine.GameEngine;
import parser.LevelCreator;
import tiles.TileType;
import timer.PowerUpTimer;
import wrappers.SystemWrapper;

public class CoinImplementationTest {

	@Mock
	private GameEngine gameEngine;

	private Coin coin1, coin2, coin3;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		coin1 = new Coin(1, 1);
		coin2 = new Coin(3, 4);
		coin3 = new Coin(5, 2);

		when(gameEngine.getCoins()).thenReturn(Arrays.asList(coin1, coin2, coin3));
	}

	private GameEngine createRealGameEngine() {
		LevelCreator levelCreator = mock(LevelCreator.class);
		SystemWrapper systemWrapper = mock(SystemWrapper.class);
		PowerUpTimer powerUpTimer = mock(PowerUpTimer.class);
		GameEngine realGameEngine = new GameEngine(levelCreator, systemWrapper, powerUpTimer);
		realGameEngine.setLevelHorizontalDimension(10);
		realGameEngine.setLevelVerticalDimension(10);
		return realGameEngine;
	}

	@Test
    public void should_return_zero_coins_when_none_are_added() {
        when(gameEngine.getCoins()).thenReturn(Collections.emptyList());
        List<Coin> coins = gameEngine.getCoins();
        assertEquals(0, coins.size(), "There should be zero coins in the game.");
    }

	@Test
	public void should_return_one_coin_when_one_coin_is_added() {
		Coin singleCoin = new Coin(5, 1);
		when(gameEngine.getCoins()).thenReturn(Collections.singletonList(singleCoin));
		List<Coin> coins = gameEngine.getCoins();

		assertEquals(1, coins.size(), "There should be exactly one coin in the game.");
		assertEquals(5, coins.get(0).getX(), "Coin X position should be correct.");
		assertEquals(1, coins.get(0).getY(), "Coin Y position should be correct.");
	}

	@Test
    public void should_return_multiple_coins_when_multiple_coins_are_added() {
        when(gameEngine.getCoins()).thenReturn(Arrays.asList(coin1, coin2, coin3));
        List<Coin> coins = gameEngine.getCoins();

        assertEquals(3, coins.size(), "There should be exactly three coins in the game.");
        assertEquals(1, coins.get(0).getX(), "Coin X position should be correct.");
        assertEquals(1, coins.get(0).getY(), "Coin Y position should be correct.");
        assertEquals(3, coins.get(1).getX(), "Coin X position should be correct.");
        assertEquals(4, coins.get(1).getY(), "Coin Y position should be correct.");
        assertEquals(5, coins.get(2).getX(), "Coin X position should be correct.");
        assertEquals(2, coins.get(2).getY(), "Coin Y position should be correct.");
    }

	@Test
	public void should_not_have_coin_overlap() {
		GameEngine realGameEngine = createRealGameEngine();
		realGameEngine.addRandomCoins(5);
		List<Coin> coins = realGameEngine.getCoins();

		Set<String> coinPositions = new HashSet<>();
		for (Coin coin : coins) {
			String position = coin.getX() + "," + coin.getY();
			assertFalse(coinPositions.contains(position),
					"Coin at position " + position + " overlaps with another coin.");
			coinPositions.add(position);
		}
	}

	@Test
	public void should_add_ten_random_coins_to_the_game() {
		GameEngine realGameEngine = createRealGameEngine();
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				realGameEngine.addTile(x, y, TileType.PASSABLE);
			}
		}
		assertEquals(0, realGameEngine.getCoins().size(), "Initially, there should be no coins in the game.");
		realGameEngine.addRandomCoins(10);
		List<Coin> coins = realGameEngine.getCoins();
		assertEquals(10, coins.size(), "There should be exactly 10 random coins added to the game.");
	}
}
