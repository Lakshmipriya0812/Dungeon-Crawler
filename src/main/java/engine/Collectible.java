package engine;

import tiles.TileType;

public abstract class Collectible {
	protected int x, y;

	protected Collectible(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public abstract TileType getTileType();
}
