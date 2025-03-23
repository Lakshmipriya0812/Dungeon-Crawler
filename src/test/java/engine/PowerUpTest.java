package engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PowerUpTest {

	@Test
	public void power_up_initialization() {
		PowerUp powerUp = new PowerUp(15, 25);
		assertEquals(15, powerUp.getX());
		assertEquals(25, powerUp.getY());
	}

	@Test
	public void to_get_x_coordinates_and_get_y_coordinates() {
		PowerUp powerUp = new PowerUp(5, 15);
		assertEquals(5, powerUp.getX());
		assertEquals(15, powerUp.getY());
	}
}
