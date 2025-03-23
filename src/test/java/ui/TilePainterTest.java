package ui;

import static org.mockito.Mockito.*;

import java.awt.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import engine.Enemy;
import engine.GameEngine;
import tiles.TileType;
import values.TileColorMap;

public class TilePainterTest {

	private final int TILE_WIDTH = 10;
	private final int TILE_HEIGHT = 20;
	private final int X = 2;
	private final int Y = 3;

	@Mock
	Graphics graphics;
	@Mock
	GameEngine gameEngine;
	@InjectMocks
	TilePainter tilePainter;
	private Enemy enemy;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		enemy = mock(Enemy.class);
		when(gameEngine.getEnemy()).thenReturn(enemy);
		when(gameEngine.getLevelHorizontalDimension()).thenReturn(X);
		when(gameEngine.getLevelVerticalDimension()).thenReturn(Y);
	}

	@Test
	public void paint_tiles() {
		GameEngine gameEngine = Mockito.mock(GameEngine.class);
		Graphics graphics = Mockito.mock(Graphics.class);
		Mockito.when(gameEngine.getLevelHorizontalDimension()).thenReturn(X);
		Mockito.when(gameEngine.getLevelVerticalDimension()).thenReturn(Y);
		Mockito.when(gameEngine.getTileFromCoordinates(1, 1)).thenReturn(TileType.NOT_PASSABLE);
		Mockito.when(gameEngine.getTileFromCoordinates(AdditionalMatchers.not(ArgumentMatchers.eq(1)),
				AdditionalMatchers.not(ArgumentMatchers.eq(1)))).thenReturn(TileType.PASSABLE);
		Enemy enemy = Mockito.mock(Enemy.class);
		Mockito.when(gameEngine.getEnemy()).thenReturn(enemy);
		Mockito.when(enemy.getX()).thenReturn(1);
		Mockito.when(enemy.getY()).thenReturn(1);
		TilePainter tilePainter = new TilePainter();
		tilePainter.paintTiles(graphics, gameEngine, TILE_WIDTH, TILE_HEIGHT);
		InOrder inOrder = Mockito.inOrder(graphics);
		inOrder.verify(graphics).setColor(TileColorMap.get(TileType.PASSABLE));
		inOrder.verify(graphics).fillRect(0, 0, 10, 20);
		inOrder.verify(graphics).fillRect(0, 20, 10, 20);
		inOrder.verify(graphics).fillRect(0, 40, 10, 20);
		inOrder.verify(graphics).fillRect(10, 0, 10, 20);
		inOrder.verify(graphics).setColor(TileColorMap.get(TileType.NOT_PASSABLE));
		inOrder.verify(graphics).fillRect(10, 20, 10, 20);
		inOrder.verify(graphics).fillRect(10, 40, 10, 20);

	}

	@Test
	public void paint_tiles_includes_coin() {
		GameEngine gameEngine = Mockito.mock(GameEngine.class);
		Graphics graphics = Mockito.mock(Graphics.class);
		Mockito.when(gameEngine.getLevelHorizontalDimension()).thenReturn(2);
		Mockito.when(gameEngine.getLevelVerticalDimension()).thenReturn(2);
		Mockito.when(gameEngine.getTileFromCoordinates(0, 0)).thenReturn(TileType.COIN);
		Mockito.when(gameEngine.getTileFromCoordinates(1, 1)).thenReturn(TileType.PASSABLE);
		Enemy enemy = Mockito.mock(Enemy.class);
		Mockito.when(gameEngine.getEnemy()).thenReturn(enemy);
		TilePainter tilePainter = new TilePainter();
		tilePainter.paintTiles(graphics, gameEngine, 10, 10);
		Mockito.verify(graphics).setColor(TileColorMap.get(TileType.COIN));
		Mockito.verify(graphics).fillOval(0, 0, 10, 10);
	}

	@Test
	public void paint_tiles_includes_enemy() {
		GameEngine gameEngine = Mockito.mock(GameEngine.class);
		Graphics graphics = Mockito.mock(Graphics.class);
		Mockito.when(gameEngine.getLevelHorizontalDimension()).thenReturn(2);
		Mockito.when(gameEngine.getLevelVerticalDimension()).thenReturn(2);
		Mockito.when(gameEngine.getTileFromCoordinates(1, 1)).thenReturn(TileType.PASSABLE);
		Enemy enemy = Mockito.mock(Enemy.class);
		Mockito.when(gameEngine.getEnemy()).thenReturn(enemy);
		Mockito.when(enemy.getX()).thenReturn(1);
		Mockito.when(enemy.getY()).thenReturn(1);
		TilePainter tilePainter = new TilePainter();
		tilePainter.paintTiles(graphics, gameEngine, 10, 10);
		InOrder inOrder = Mockito.inOrder(graphics);
		inOrder.verify(graphics).setColor(TileColorMap.get(TileType.PASSABLE));
		inOrder.verify(graphics).fillRect(10, 10, 10, 10);
		inOrder.verify(graphics).setColor(TileColorMap.get(TileType.ENEMY));
		inOrder.verify(graphics).fillRect(10, 10, 10, 10);
	}

	@Test
	public void paint_player() {
		Point playerPosition = new Point(X, Y);
		Rectangle tileDimensions = new Rectangle(TILE_WIDTH, TILE_HEIGHT);

		tilePainter.paintPlayer(graphics, playerPosition, tileDimensions, TileType.PLAYER);

		verify(graphics).fillRect(20, 60, TILE_WIDTH, TILE_HEIGHT);
	}

	@Test
	public void paint_coin() {
		tilePainter.paintCoin(graphics, new Rectangle(X * TILE_WIDTH, Y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT));

		verify(graphics).setColor(Color.YELLOW);
		verify(graphics).fillOval(X * TILE_WIDTH, Y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
	}

	@Test
		public void paint_enemy() {
			when(gameEngine.getTileFromCoordinates(0, 0)).thenReturn(TileType.ENEMY);
			when(enemy.getX()).thenReturn(0);
			when(enemy.getY()).thenReturn(0);

			tilePainter.paintTiles(graphics, gameEngine, TILE_WIDTH, TILE_HEIGHT);

			InOrder inOrder = inOrder(graphics);
			inOrder.verify(graphics).setColor(Color.RED);
			inOrder.verify(graphics).fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
		}

	@Test
		public void paint_tiles_correctly_calls_paintCoin() {
			when(gameEngine.getTileFromCoordinates(0, 0)).thenReturn(TileType.COIN);

			TilePainter spyTilePainter = spy(new TilePainter());
			spyTilePainter.paintTiles(graphics, gameEngine, TILE_WIDTH, TILE_HEIGHT);

			verify(spyTilePainter).paintCoin(graphics, new Rectangle(0, 0, TILE_WIDTH, TILE_HEIGHT));
		}

	@Test
		public void paint_tiles_correctly_calls_paintEnemy() {
			when(gameEngine.getTileFromCoordinates(0, 0)).thenReturn(TileType.ENEMY);
			when(gameEngine.getTileFromCoordinates(1, 1)).thenReturn(TileType.NOT_PASSABLE);

			TilePainter spyTilePainter = spy(new TilePainter());
			spyTilePainter.paintTiles(graphics, gameEngine, TILE_WIDTH, TILE_HEIGHT);

			verify(spyTilePainter).paintEnemy(graphics, new Rectangle(0, 0, TILE_WIDTH, TILE_HEIGHT));
		}

	@Test
		public void paint_tiles_other_tile_types() {
			when(gameEngine.getTileFromCoordinates(0, 0)).thenReturn(TileType.PASSABLE);
			when(gameEngine.getTileFromCoordinates(1, 0)).thenReturn(TileType.NOT_PASSABLE);

			tilePainter.paintTiles(graphics, gameEngine, TILE_WIDTH, TILE_HEIGHT);

			InOrder inOrder = inOrder(graphics);
			inOrder.verify(graphics).setColor(TileColorMap.get(TileType.PASSABLE));
			inOrder.verify(graphics).fillRect(0, 0, TILE_WIDTH, TILE_HEIGHT);
			inOrder.verify(graphics).setColor(TileColorMap.get(TileType.NOT_PASSABLE));
			inOrder.verify(graphics).fillRect(TILE_WIDTH, 0, TILE_WIDTH, TILE_HEIGHT);
		}

	@Test
	public void paint_power_up() {
		tilePainter.paintPowerUp(graphics, new Rectangle(X * TILE_WIDTH, Y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT));

		verify(graphics).setColor(Color.BLUE);
		verify(graphics).fillOval(X * TILE_WIDTH, Y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
	}

	@Test
	public void paint_tiles_includes_powerUp() {
		GameEngine gameEngine = Mockito.mock(GameEngine.class);
		Graphics graphics = Mockito.mock(Graphics.class);
		Mockito.when(gameEngine.getLevelHorizontalDimension()).thenReturn(2);
		Mockito.when(gameEngine.getLevelVerticalDimension()).thenReturn(2);
		Mockito.when(gameEngine.getTileFromCoordinates(0, 0)).thenReturn(TileType.POWER_UP);
		Mockito.when(gameEngine.getTileFromCoordinates(1, 1)).thenReturn(TileType.PASSABLE);
		Enemy enemy = Mockito.mock(Enemy.class);
		Mockito.when(gameEngine.getEnemy()).thenReturn(enemy);
		TilePainter tilePainter = new TilePainter();
		tilePainter.paintTiles(graphics, gameEngine, 10, 10);
		Mockito.verify(graphics).setColor(Color.BLUE);
		Mockito.verify(graphics).fillOval(0, 0, 10, 10);
	}

	@Test
	public void paint_tiles_correctly_calls_paintPowerUp() {
		when(gameEngine.getTileFromCoordinates(0, 0)).thenReturn(TileType.POWER_UP);

		TilePainter spyTilePainter = spy(new TilePainter());
		spyTilePainter.paintTiles(graphics, gameEngine, TILE_WIDTH, TILE_HEIGHT);

		verify(spyTilePainter).paintPowerUp(graphics, new Rectangle(0, 0, TILE_WIDTH, TILE_HEIGHT));
	}

	@Test
	public void paintPowerUp_does_not_paint_for_nonPowerUpTiles() {
		GameEngine gameEngine = Mockito.mock(GameEngine.class);
		Graphics graphics = Mockito.mock(Graphics.class);
		Mockito.when(gameEngine.getLevelHorizontalDimension()).thenReturn(2);
		Mockito.when(gameEngine.getLevelVerticalDimension()).thenReturn(2);
		Mockito.when(gameEngine.getTileFromCoordinates(0, 0)).thenReturn(TileType.PASSABLE);
		Enemy enemy = Mockito.mock(Enemy.class);
		Mockito.when(gameEngine.getEnemy()).thenReturn(enemy);
		TilePainter tilePainter = new TilePainter();
		tilePainter.paintTiles(graphics, gameEngine, 10, 10);
		verify(graphics, times(0)).setColor(Color.BLUE);
		verify(graphics, times(0)).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
	}

}
