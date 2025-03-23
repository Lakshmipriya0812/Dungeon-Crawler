package engine;

import java.awt.*;
import java.util.Map;

import tiles.TileType;
import values.TunableParameters;
import wrappers.SystemWrapper;

public class Enemy {
	private long lastMoveTime = 0;
	private Point position;
	private boolean movingUp = true;
	private SystemWrapper systemWrapper;

	public Enemy(int x, int y, SystemWrapper systemWrapper) {
		this.position = new Point(x, y);
		this.systemWrapper = systemWrapper;
	}

	public int getX() {
		return (int) position.getX();
	}

	public int getY() {
		return (int) position.getY();
	}

	public void enemyMovements(Map<Point, TileType> tiles, int levelHeight, int levelWidth) {
		long currentTime = systemWrapper.currentTimeMillis();
		if (!isTimeToMove(currentTime)) {
			return;
		}
		int currentX = getX();
		int currentY = getY();
		int nextY = calculateNextY(currentY);

		if (isPositionWithinBounds(nextY, levelHeight)) {
			handleMovement(tiles, levelWidth, currentX, currentY, nextY);
		}
		lastMoveTime = currentTime;
	}

	private void handleMovement(Map<Point, TileType> tiles, int levelWidth, int currentX, int currentY, int nextY) {
		Point nextPosition = new Point(currentX, nextY);
		TileType nextTile = getTileType(tiles, nextPosition);

		if (isPassable(nextTile)) {
			moveToNextPosition(tiles, nextPosition);
		} else if (isNotPassable(nextTile)) {
			handleNotPassableTile(currentX, currentY, levelWidth);
		}
	}

	private void moveToNextPosition(Map<Point, TileType> tiles, Point nextPosition) {
		updateTileToPassable(tiles);
		position.setLocation(nextPosition);
	}

	boolean isTimeToMove(long currentTime) {
		return currentTime - lastMoveTime >= TunableParameters.MOVE_DELAY;
	}

	private int calculateNextY(int currentY) {
		return movingUp ? currentY - 1 : currentY + 1;
	}

	boolean isPositionWithinBounds(int nextY, int levelHeight) {
		return nextY >= 0 && nextY < levelHeight;
	}

	private TileType getTileType(Map<Point, TileType> tiles, Point position) {
		return tiles.getOrDefault(position, TileType.NOT_PASSABLE);
	}

	private boolean isPassable(TileType tileType) {
		return tileType == TileType.PASSABLE || tileType == TileType.COIN || tileType == TileType.POWER_UP;
	}

	private void updateTileToPassable(Map<Point, TileType> tiles) {
		TileType currentTileType = getTileType(tiles, position);
		if (currentTileType == TileType.ENEMY) {
			tiles.put(position, TileType.PASSABLE);
		}
	}

	private boolean isNotPassable(TileType tileType) {
		return tileType == TileType.NOT_PASSABLE;
	}

	private void handleNotPassableTile(int currentX, int currentY, int levelWidth) {
		int newX = (currentX + 1) % levelWidth;
		position.setLocation(newX, currentY);
		movingUp = !movingUp;
	}
}