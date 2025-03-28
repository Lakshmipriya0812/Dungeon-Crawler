package values;

import java.awt.*;
import java.util.EnumMap;

import tiles.TileType;

public final class TileColorMap {
	private static final EnumMap<TileType, Color> tileColors = new EnumMap<>(TileType.class);

	static {
		tileColors.put(TileType.PASSABLE, Color.WHITE);
		tileColors.put(TileType.NOT_PASSABLE, Color.BLACK);
		tileColors.put(TileType.PLAYER, Color.GREEN);
		tileColors.put(TileType.COIN, Color.YELLOW);
		tileColors.put(TileType.ENEMY, Color.RED);
		tileColors.put(TileType.POWER_UP, Color.BLUE);
	}

	private TileColorMap() {
	}

	public static Color get(TileType key) {
		return tileColors.get(key);
	}
}