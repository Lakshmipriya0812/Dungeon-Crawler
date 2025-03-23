package engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CoinTest {
	@Test
	public void coin_initialization() {
		Coin coin = new Coin(10, 20);
		assertEquals(10, coin.getX());
		assertEquals(20, coin.getY());
	}

	@Test
	public void to_get_x_coordinates_and_get_y_coordinates() {
		Coin coin = new Coin(5, 15);
		assertEquals(5, coin.getX());
		assertEquals(15, coin.getY());
	}

}
