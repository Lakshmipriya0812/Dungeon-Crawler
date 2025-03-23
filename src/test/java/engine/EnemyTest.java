package engine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tiles.TileType;
import wrappers.SystemWrapper;

class EnemyTest {

	private Enemy enemy;
	private Map<Point, TileType> tiles;
	private int levelHeight;
	private int levelWidth;
	@Mock
	private SystemWrapper systemWrapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		levelHeight = 10;
		levelWidth = 10;
		tiles = new HashMap<>();
		for (int y = 0; y < levelHeight; y++) {
			for (int x = 0; x < levelWidth; x++) {
				tiles.put(new Point(x, y), TileType.PASSABLE);
			}
		}
		enemy = new Enemy(5, 10, systemWrapper);
		tiles.put(new Point(5, 5), TileType.ENEMY);
	}

	@Test
	public void enemy_initialization() {
		assertEquals(5, enemy.getX(), "The X-coordinate should be 5");
		assertEquals(10, enemy.getY(), "The Y-coordinate should be 10");
	}

	@Test
	public void get_enemy_x_position() {
		assertEquals(5, enemy.getX(), "The X-coordinate should be 5");
	}

	@Test
	public void get_enemy_y_position() {
		assertEquals(10, enemy.getY(), "The Y-coordinate should be 10");
	}

	@Test
	public void check_enemy_move_up_from_start_point()  {
		when(systemWrapper.currentTimeMillis()).thenReturn(1000L, 1100L);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 10), TileType.ENEMY);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(9, enemy.getY(), "The Y-coordinate should be 9 after moving up.");
		assertEquals(5, enemy.getX(), "The Y-coordinate should be 9 after moving up.");
		verify(systemWrapper, times(1)).currentTimeMillis();

	}

	@Test
	public void check_enemy_crossing_coin_without_disturbing_coin_placement(){
		when(systemWrapper.currentTimeMillis()).thenReturn(1000L, 1100L);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 11), TileType.COIN);
		tiles.put(new Point(5, 12), TileType.NOT_PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(TileType.COIN, tiles.get(new Point(5, 11)), "The coin should remain at its position.");
		verify(systemWrapper, times(1)).currentTimeMillis();

	}

	@Test
	public void check_enemy_crossing_power_up_without_disturbing_power_up_placement() {
		when(systemWrapper.currentTimeMillis()).thenReturn(1000L, 1100L);
		tiles.put(new Point(6, 9), TileType.PASSABLE);
		tiles.put(new Point(6, 10), TileType.ENEMY);
		tiles.put(new Point(6, 11), TileType.POWER_UP);
		tiles.put(new Point(6, 12), TileType.NOT_PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(TileType.POWER_UP, tiles.get(new Point(6, 11)), "The coin should remain at its position.");
		verify(systemWrapper, times(1)).currentTimeMillis();

	}

	@Test
	public void check_enemy_movement_move_up_passing_three_tiles() {
		when(systemWrapper.currentTimeMillis())
				.thenReturn(1000L, 6000L, 11000L, 16000L);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 8), TileType.PASSABLE);
		tiles.put(new Point(5, 7), TileType.PASSABLE);
		tiles.put(new Point(5, 6), TileType.NOT_PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(7, enemy.getY(), "Enemy should stop at Y=7 after moving up 3 tiles.");
		assertEquals(5, enemy.getX(), "Enemy's X-coordinate should remain 5.");
		verify(systemWrapper, times(3)).currentTimeMillis();
	}

	@Test
	public void check_enemy_move_horizontal_once_reached_not_passable_tile() {
		when(systemWrapper.currentTimeMillis()).thenReturn(1000L, 6000L, 11000L);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 8), TileType.NOT_PASSABLE);
		tiles.put(new Point(6, 9), TileType.PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(9, enemy.getY(), "Enemy's Y-coordinate should remain 9 after moving horizontally.");
		assertEquals(6, enemy.getX(), "Enemy's X-coordinate should be 6 after moving right.");
		verify(systemWrapper, times(2)).currentTimeMillis();

	}

	@Test
	public void check_enemy_avoids_non_passable_tile(){
		when(systemWrapper.currentTimeMillis()).thenReturn(1000L, 6000L, 11000L);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 8), TileType.NOT_PASSABLE);
		tiles.put(new Point(6, 9), TileType.PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(9, enemy.getY(), "Enemy's Y-coordinate should remain 9 after avoiding the block.");
		assertEquals(6, enemy.getX(), "Enemy should move horizontally to X=6 after encountering a block.");
		verify(systemWrapper, times(2)).currentTimeMillis();
	}

	@Test
	public void check_enemy_move_down_after_horizontal_move(){
		when(systemWrapper.currentTimeMillis())
				.thenReturn(1000L, 6000L, 11000L, 16000L);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 8), TileType.NOT_PASSABLE);
		tiles.put(new Point(6, 9), TileType.PASSABLE);
		tiles.put(new Point(6, 10), TileType.PASSABLE);
		tiles.put(new Point(6, 11), TileType.NOT_PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(10, enemy.getY(), "Enemy should move down to Y=10 after horizontal shift.");
		assertEquals(6, enemy.getX(), "Enemy's X-coordinate should remain 6 after horizontal shift.");
		verify(systemWrapper, times(3)).currentTimeMillis();
	}

	@Test
	public void check_enemy_wraps_around_horizontally(){
		when(systemWrapper.currentTimeMillis())
				.thenReturn(1000L, 6000L);
		Enemy enemy = new Enemy(19, 10, systemWrapper);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(19, 10), TileType.ENEMY);
		tiles.put(new Point(19, 9), TileType.NOT_PASSABLE);
		tiles.put(new Point(0, 10), TileType.PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(10, enemy.getY(), "Enemy's Y-coordinate should remain 10.");
		assertEquals(0, enemy.getX(), "Enemy should wrap around to X=0.");
		verify(systemWrapper, times(1)).currentTimeMillis();
	}

	@Test
	public void check_enemy_moves_down_after_shifting_right()  {
		when(systemWrapper.currentTimeMillis())
				.thenReturn(1000L, 6000L, 11000L, 16000L);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 8), TileType.NOT_PASSABLE);
		tiles.put(new Point(6, 9), TileType.PASSABLE);
		tiles.put(new Point(6, 10), TileType.PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(10, enemy.getY(), "Enemy should move down to Y=10.");
		assertEquals(6, enemy.getX(), "Enemy's X-coordinate should remain 6.");
		verify(systemWrapper, times(3)).currentTimeMillis();
	}

	@Test
	public void check_enemy_moves_horizontally_after_block() {
		when(systemWrapper.currentTimeMillis())
				.thenReturn(1000L, 6000L, 11000L);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 8), TileType.NOT_PASSABLE);
		tiles.put(new Point(6, 9), TileType.PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(9, enemy.getY(), "Enemy should remain at Y=9 after shifting horizontally.");
		assertEquals(6, enemy.getX(), "Enemy should move horizontally to X=6.");
		verify(systemWrapper, times(2)).currentTimeMillis();

	}

	@Test
	public void check_enemy_moves_up_until_blocked(){
		when(systemWrapper.currentTimeMillis())
				.thenReturn(1000L, 6000L, 11000L);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 8), TileType.PASSABLE);
		tiles.put(new Point(5, 7), TileType.NOT_PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(8, enemy.getY(), "Enemy should stop at Y=8 before hitting the non-passable tile.");
		assertEquals(5, enemy.getX(), "Enemy's X-coordinate should remain 5.");
		verify(systemWrapper, times(2)).currentTimeMillis();
	}

	@Test
	public void check_enemy_at_top_left_edge() {
		when(systemWrapper.currentTimeMillis())
				.thenReturn(1000L, 6000L);
		Enemy enemy = new Enemy(0, 0, systemWrapper);
		HashMap<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(0, 0), TileType.ENEMY);
		tiles.put(new Point(0, 1), TileType.PASSABLE);
		tiles.put(new Point(1, 0), TileType.PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(0, enemy.getY(), "Enemy should not move up past Y=0.");
		assertEquals(0, enemy.getX(), "Enemy should not move left past X=0.");
		verify(systemWrapper, times(1)).currentTimeMillis();
	}

	@Test
	public void check_enemy_does_not_collect_power_up() {
		when(systemWrapper.currentTimeMillis())
				.thenReturn(1000L, 6000L);
		Map<Point, TileType> tiles = new HashMap<>();
		tiles.put(new Point(5, 9), TileType.PASSABLE);
		tiles.put(new Point(5, 10), TileType.ENEMY);
		tiles.put(new Point(5, 11), TileType.POWER_UP);
		tiles.put(new Point(5, 12), TileType.NOT_PASSABLE);
		enemy.enemyMovements(tiles, 20, 20);
		assertEquals(TileType.POWER_UP, tiles.get(new Point(5, 11)));
		verify(systemWrapper, times(1)).currentTimeMillis();

	}

	@Test
	public void check_method_time_to_move_edge_boundary() {
		when(systemWrapper.currentTimeMillis()).thenReturn(1000L, 1100L);
		boolean result = enemy.isTimeToMove(systemWrapper.currentTimeMillis());
		assertTrue(result);
	}

	@Test
	public void check_method_position_within_bounds_edge_case() {
		assertTrue(enemy.isPositionWithinBounds(0, levelHeight));
		assertTrue(enemy.isPositionWithinBounds(levelHeight - 1, levelHeight));
		assertFalse(enemy.isPositionWithinBounds(levelHeight, levelHeight));
	}
}
