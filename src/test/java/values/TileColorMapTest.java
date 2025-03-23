package values;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.*;

import org.junit.jupiter.api.Test;

import tiles.TileType;

public class TileColorMapTest {

	@Test
	public void passable() {
		assertSame(Color.WHITE, TileColorMap.get(TileType.PASSABLE));
	}

	@Test
	public void not_passable() {
		assertSame(Color.BLACK, TileColorMap.get(TileType.NOT_PASSABLE));
	}

	@Test
	public void player() {
		assertSame(Color.GREEN, TileColorMap.get(TileType.PLAYER));
	}

	@Test
	public void coin() {
		assertSame(Color.YELLOW, TileColorMap.get(TileType.COIN));
	}

	@Test
	public void enemy() {
		assertSame(Color.RED, TileColorMap.get(TileType.ENEMY));
	}

	@Test
	public void power_up() {
		assertSame(Color.BLUE, TileColorMap.get(TileType.POWER_UP));
	}

}
