package tiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TileTypeTest {

	private static final char INVALID_CHAR = 'Z';
	private static final char VALID_CHAR = ' ';
	private static final char COIN_CHAR = 'C';
	private static final char ENEMY_CHAR = 'E';
	private static final char POWER_UP_CHAR = 'U';

	@Test
	public void value_of() {
		assertThat(TileType.valueOf(TileType.PASSABLE.name()), equalTo(TileType.PASSABLE));
	}

	@Test
	public void get_tile_type_by_char_valid_char() {
		TileType actual = TileType.getTileTypeByChar(VALID_CHAR);
		assertEquals(TileType.PASSABLE, actual);
	}

	@Test
	public void get_tile_type_by_char_invalid_char() {
		try {
			TileType.getTileTypeByChar(INVALID_CHAR);
		} catch (IllegalArgumentException exception) {
			assertEquals(exception.getMessage(), TileType.INVALID_CHARACTER_PROVIDED_MESSAGE + "Z");
		}
	}

	@Test
	public void get_tile_type_by_char_for_coin() {
		TileType actual = TileType.getTileTypeByChar(COIN_CHAR);
		assertEquals(TileType.COIN, actual);
	}

	@Test
	public void get_tile_type_by_char_for_enemy() {
		TileType actual = TileType.getTileTypeByChar(ENEMY_CHAR);
		assertEquals(TileType.ENEMY, actual);
	}

	@Test
	public void get_tile_type_by_char_for_power_up() {
		TileType actual = TileType.getTileTypeByChar(POWER_UP_CHAR);
		assertEquals(TileType.POWER_UP, actual);
	}
}
