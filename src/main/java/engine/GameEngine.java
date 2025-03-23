package engine;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.LevelCreator;
import tiles.TileType;
import timer.PowerUpTimer;
import ui.GameFrame;
import values.TunableParameters;
import wrappers.SystemWrapper;

public class GameEngine {

	private static final String COIN = "Coin";
	private static final String POWER_UP = "PowerUp";
	protected final Map<Point, TileType> tiles = new HashMap<>();
	private final LevelCreator levelCreator;
	private final int level;
	private List<Coin> coins = new ArrayList<>();
	private boolean exit;
	private int levelHorizontalDimension;
	private int levelVerticalDimension;
	private Point player;
	private int collectedCoins = 0;
	private Enemy enemy;
	private List<PowerUp> powerUpPositions = new ArrayList<>();
	private int collectedPowerUps = 0;
	private PowerUpTimer powerUpTimer;
	private SystemWrapper systemWrapper;

	public GameEngine(LevelCreator levelCreator, SystemWrapper systemWrapper, PowerUpTimer powerUpTimer) {
		exit = false;
		level = 1;
		this.levelCreator = levelCreator;
		this.systemWrapper = systemWrapper;
		this.levelCreator.createLevel(this, level);
		initializeEnemy(8, levelVerticalDimension - 3);
		this.powerUpTimer = powerUpTimer;
	}

	public void run(GameFrame gameFrame) {
		enemy.enemyMovements(tiles, levelVerticalDimension, levelHorizontalDimension);
		checkForEnemyPlayerCollision();
		for (Component component : gameFrame.getComponents()) {
			component.repaint();
		}
	}

	public void addTile(int x, int y, TileType tileType) {
		if (tileType.equals(TileType.PLAYER)) {
			setPlayer(x, y);
			tiles.put(new Point(x, y), TileType.PASSABLE);
		} else {
			tiles.put(new Point(x, y), tileType);
		}
	}

	public void playerMoves(int xMove, int yMove) {
		int moveFactor = isPlayerDoubleMoveActive() ? 2 : 1;
		int newX = getPlayerXCoordinate() + xMove * moveFactor;
		int newY = getPlayerYCoordinate() + yMove * moveFactor;
		if (Math.abs(newX - getPlayerXCoordinate()) > 2) {
			newX = getPlayerXCoordinate() + 2 * Integer.signum(xMove);
		}
		if (Math.abs(newY - getPlayerYCoordinate()) > 2) {
			newY = getPlayerYCoordinate() + 2 * Integer.signum(yMove);
		}
		if (doubleMoveValid(xMove, yMove, moveFactor, getPlayerXCoordinate(), getPlayerYCoordinate())) {
			handleTileAction(newX, newY);
		}
	}

	private void handleTileAction(int newX, int newY) {
		TileType nextTile = getTileFromCoordinates(newX, newY);

		if (nextTile.equals(TileType.PASSABLE) || nextTile.equals(TileType.COIN)
				|| nextTile.equals(TileType.POWER_UP)) {
			setPlayer(newX, newY);
		}
		if (nextTile.equals(TileType.COIN)) {
			collectItem(newX, newY, COIN);
		} else if (nextTile.equals(TileType.POWER_UP)) {
			collectItem(newX, newY, POWER_UP);
		}
	}

	public int getLevelHorizontalDimension() {
		return levelHorizontalDimension;
	}

	public void setLevelHorizontalDimension(int levelHorizontalDimension) {
		this.levelHorizontalDimension = levelHorizontalDimension;
	}

	public int getLevelVerticalDimension() {
		return levelVerticalDimension;
	}

	public void setLevelVerticalDimension(int levelVerticalDimension) {
		this.levelVerticalDimension = levelVerticalDimension;
	}

	public TileType getTileFromCoordinates(int x, int y) {
		return tiles.get(new Point(x, y));
	}

	public void setPlayer(int x, int y) {
		player = new Point(x, y);
	}

	public int getPlayerXCoordinate() {
		return (int) player.getX();
	}

	public int getPlayerYCoordinate() {
		return (int) player.getY();
	}

	public void keyLeft() {
		playerMoves(-1, 0);

	}

	public void keyRight() {
		playerMoves(+1, 0);

	}

	public void keyUp() {
		playerMoves(0, -1);
	}

	public void keyDown() {
		playerMoves(0, +1);
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;

	}

	public void addCollectible(int x, int y, Collectible collectible) {
		validatePlacement(x, y, collectible);

		if (collectible instanceof Coin) {
			coins.add((Coin) collectible);
		} else if (collectible instanceof PowerUp) {
			powerUpPositions.add((PowerUp) collectible);
		}

		placeTile(x, y, collectible.getTileType());
	}

	private void validatePlacement(int x, int y, Collectible collectible) {
		checkIfCollectibleAlreadyExists(x, y, collectible);
		validateTilePlacement(x, y, collectible);
	}

	private void checkIfCollectibleAlreadyExists(int x, int y, Collectible collectible) {
		String type = collectible instanceof Coin ? COIN : POWER_UP;

		if (type.equals(COIN) && isCoinAtPosition(x, y)) {
			throw new IllegalArgumentException("Coin already exists at this position.");
		}

		if (type.equals(POWER_UP) && isPowerUpAtPosition(x, y)) {
			throw new IllegalArgumentException("PowerUp already exists at this position.");
		}
	}

	private void validateTilePlacement(int x, int y, Collectible collectible) {
		TileType tile = getTileFromCoordinates(x, y);
		TileType validTile = TileType.PASSABLE;
		TileType restrictedTile = (collectible instanceof Coin) ? TileType.POWER_UP : TileType.COIN;

		if (tile != validTile) {
			String errorMessage = (tile == restrictedTile)
					? "Cannot place " + getCollectibleType(collectible) + " on a " + restrictedTile.name().toLowerCase()
							+ " tile."
					: "Cannot place " + getCollectibleType(collectible) + " on a non-passable tile.";
			throw new IllegalArgumentException(errorMessage);
		}
	}

	private String getCollectibleType(Collectible collectible) {
		return collectible instanceof Coin ? COIN : POWER_UP;
	}

	public void addCoin(int x, int y) {
		addCollectible(x, y, new Coin(x, y));
	}

	public void addPowerUp(int x, int y) {
		addCollectible(x, y, new PowerUp(x, y));
	}

	private void placeTile(int x, int y, TileType tileType) {
		addTile(x, y, tileType);
	}

	public List<Coin> getCoins() {
		return coins;
	}

	public List<PowerUp> getPowerUps() {
		return powerUpPositions;
	}

	public int getCollectedCoins() {
		return collectedCoins;
	}

	public int getCollectedPowerUps() {
		return collectedPowerUps;
	}

	private boolean isPositionPresent(int x, int y, List<? extends Collectible> items) {
		for (Collectible item : items) {
			if (item.getX() == x && item.getY() == y) {
				return true;
			}
		}
		return false;
	}

	public boolean isCoinAtPosition(int x, int y) {
		return isPositionPresent(x, y, coins);
	}

	public boolean isPowerUpAtPosition(int x, int y) {
		return isPositionPresent(x, y, powerUpPositions);
	}

	public boolean hasEnoughPassableTiles(int requiredPassableTiles) {
		int passableCount = 0;
		for (int x = 0; x < levelHorizontalDimension; x++) {
			for (int y = 0; y < levelVerticalDimension; y++) {
				passableCount += countPassableTilesAt(x, y);
				if (passableCount >= requiredPassableTiles) {
					return true;
				}
			}
		}
		return false;
	}

	private int countPassableTilesAt(int x, int y) {
		TileType tile = getTileFromCoordinates(x, y);
		int count = 0;
		if (isTilePassable(tile)) {
			count++;
		} else if (isSpecialTile(tile)) {
			count += checkTileBelowForPassable(x, y);
		}
		return count;
	}

	private boolean isTilePassable(TileType tile) {
		return tile == TileType.PASSABLE;
	}

	private boolean isSpecialTile(TileType tile) {
		return tile == TileType.COIN || tile == TileType.POWER_UP || tile == TileType.ENEMY;
	}

	private int checkTileBelowForPassable(int x, int y) {
		TileType tileBelow = getTileFromCoordinates(x, y);
		return tileBelow == TileType.PASSABLE ? 1 : 0;
	}

	public void addRandomCollectibles(int numberOfCollectibles, String type) {
		if (!hasEnoughPassableTiles(numberOfCollectibles)) {
			System.out
					.println("There are not enough passable tiles to place " + numberOfCollectibles + " " + type + ".");
			return;
		}

		int addedCollectibles = 0;
		while (addedCollectibles < numberOfCollectibles) {
			int x = (int) (Math.random() * levelHorizontalDimension);
			int y = (int) (Math.random() * levelVerticalDimension);

			if (getTileFromCoordinates(x, y) == TileType.PASSABLE
					&& (type.equals(POWER_UP) ? !isPowerUpAtPosition(x, y) : !isCoinAtPosition(x, y))) {
				if (type.equals(POWER_UP)) {
					addPowerUp(x, y);
				} else if (type.equals(COIN)) {
					addCoin(x, y);
				}
				addedCollectibles++;
			}
		}
	}

	public void addRandomPowerUps(int numberOfPowerUps) {
		addRandomCollectibles(numberOfPowerUps, POWER_UP);
	}

	public void addRandomCoins(int numberOfCoins) {
		addRandomCollectibles(numberOfCoins, COIN);
	}

	public void collectItem(int x, int y, String itemType) {
		if (!isCollectItemAtPosition(x, y, itemType)) {
			return;
		}
		if (itemType.equals(COIN)) {
			removeItemAtPosition(x, y, COIN);
			incrementCollectedCoins();
			updateTileStateAfterCollection(x, y);
			evaluateLevelCompletion();
		} else if (itemType.equals(POWER_UP)) {
			removeItemAtPosition(x, y, POWER_UP);
			incrementCollectedPowerUps();
			updateTileStateAfterCollection(x, y);
			powerUpTimer.activateDoubleMove(TunableParameters.POWER_UP_DURATION);
		}
	}

	public boolean isCollectItemAtPosition(int x, int y, String itemType) {
		if (itemType.equals(COIN)) {
			for (Coin coin : coins) {
				if (coin.getX() == x && coin.getY() == y) {
					return true;
				}
			}
		} else if (itemType.equals(POWER_UP)) {
			for (PowerUp powerUp : powerUpPositions) {
				if (powerUp.getX() == x && powerUp.getY() == y) {
					return true;
				}
			}
		}
		return false;
	}

	private void removeItemAtPosition(int x, int y, String itemType) {
		if (itemType.equals(COIN)) {
			coins.removeIf(coin -> coin.getX() == x && coin.getY() == y);
		} else if (itemType.equals(POWER_UP)) {
			powerUpPositions.removeIf(powerUp -> powerUp.getX() == x && powerUp.getY() == y);
		}
	}

	private void incrementCollectedCoins() {
		collectedCoins++;
	}

	private void incrementCollectedPowerUps() {
		collectedPowerUps++;
	}

	private void updateTileStateAfterCollection(int x, int y) {
		addTile(x, y, TileType.PASSABLE);
	}

	void evaluateLevelCompletion() {
		if (collectedCoins == 10) {
			handleLevelCompletion();
		} else {
			handleLevelNotCompleted();
		}
	}

	void handleLevelCompletion() {
		System.out.println("Level Completed!");
		System.out.println("Game Over! All coins collected.");
		setExit(true);
	}

	void handleLevelNotCompleted() {
		if (collectedCoins <= 10) {
			System.out.println("Level not Completed. Coins collected: " + collectedCoins + "/10");
		}
	}

	private boolean doubleMoveValid(int xMove, int yMove, int moveFactor, int playerX, int playerY) {
		int maxMove = 2;
		int totalMoveX = playerX + xMove * moveFactor;
		int totalMoveY = playerY + yMove * moveFactor;
		if (Math.abs(totalMoveX - playerX) > maxMove || Math.abs(totalMoveY - playerY) > maxMove) {
			String errorMessage = String.format("Move exceeds 2 steps. Attempted X Move: %d, Y Move: %d", xMove, yMove);
			throw new IllegalArgumentException(errorMessage);
		}
		return true;
	}

	public boolean isPlayerDoubleMoveActive() {
		return powerUpTimer.isDoubleMoveActive();
	}

	public void initializeEnemy(int x, int y) {
		this.enemy = new Enemy(x, y, systemWrapper);
		tiles.put(new Point(x, y), TileType.ENEMY);
	}

	public Enemy getEnemy() {
		return enemy;
	}

	void checkForEnemyPlayerCollision() {
		if (enemy.getX() == getPlayerXCoordinate() && enemy.getY() == getPlayerYCoordinate()) {
			setExit(true);
		}
	}
}
