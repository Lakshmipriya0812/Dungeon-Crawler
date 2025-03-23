package ui;

import java.awt.*;

import engine.GameEngine;

public class CoinDisplay {
	private static final Font FIXED_FONT = new Font("Arial", Font.BOLD, 20);
	private GameEngine gameEngine;

	public CoinDisplay(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}

	public void draw(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.setFont(FIXED_FONT);
		graphics.drawString("Coins Collected: " + gameEngine.getCollectedCoins(), 50, 80);
	}
}
