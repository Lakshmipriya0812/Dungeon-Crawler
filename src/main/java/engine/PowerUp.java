package engine;

import tiles.TileType;

public class PowerUp extends Collectible {
	public PowerUp(int x, int y) {
		super(x, y);
	}

	@Override
	public TileType getTileType() {
		return TileType.POWER_UP;
	}
}
