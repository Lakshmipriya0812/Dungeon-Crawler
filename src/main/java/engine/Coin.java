package engine;

import tiles.TileType;

public class Coin extends Collectible {
	public Coin(int x, int y) {
		super(x, y);
	}

	@Override
	public TileType getTileType() {
		return TileType.COIN;
	}
}