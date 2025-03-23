package ui;

import java.awt.*;

import engine.Enemy;
import engine.GameEngine;
import tiles.TileType;
import values.TileColorMap;

public class TilePainter {

	void paintTiles(Graphics graphics, GameEngine game, int tileWidth, int tileHeight) {
		for (int x = 0; x < game.getLevelHorizontalDimension(); x++) {
			for (int y = 0; y < game.getLevelVerticalDimension(); y++) {
				paintSingleTile(graphics, game, x, y, tileWidth, tileHeight);
			}
		}
	}

	private void paintSingleTile(Graphics graphics, GameEngine game, int x, int y, int tileWidth, int tileHeight) {
		TileType tileType = game.getTileFromCoordinates(x, y);
		Rectangle rect = createRectangle(x, y, tileWidth, tileHeight);

		if (tileType == TileType.COIN) {
			paintCoin(graphics, rect);
		} else if (tileType == TileType.POWER_UP) {
			paintPowerUp(graphics, rect);
		} else {
			paintTile(graphics, rect, tileType);
		}

		if (isEnemyAtPosition(game.getEnemy(), x, y)) {
			paintEnemy(graphics, rect);
		}
	}

	private Rectangle createRectangle(int x, int y, int tileWidth, int tileHeight) {
		return new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
	}

	private boolean isEnemyAtPosition(Enemy enemy, int x, int y) {
		return enemy.getX() == x && enemy.getY() == y;
	}

	protected void paintPowerUp(Graphics graphics, Rectangle rect) {
		graphics.setColor(Color.BLUE);
		graphics.fillOval(rect.x, rect.y, rect.width, rect.height);
	}

	protected void paintCoin(Graphics graphics, Rectangle rect) {
		graphics.setColor(Color.YELLOW);
		graphics.fillOval(rect.x, rect.y, rect.width, rect.height);
	}

	void paintEnemy(Graphics graphics, Rectangle rect) {
		graphics.setColor(Color.RED);
		graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
	}

	private void paintTile(Graphics graphics, Rectangle rect, TileType tileType) {
		graphics.setColor(TileColorMap.get(tileType));
		graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
	}

	void paintPlayer(Graphics graphics, Point position, Rectangle dimensions, TileType tileType) {
		paintTile(graphics, new Rectangle(position.x * dimensions.width, position.y * dimensions.height,
				dimensions.width, dimensions.height), tileType);
	}
}
