package ui;

import java.awt.*;

import engine.GameEngine;
import tiles.TileType;

public class GamePanel extends Panel {

	private static final long serialVersionUID = 1L;
	private final GameEngine gameEngine;
	private final TilePainter tilePainter;
	private final CoinDisplay coinDisplay;
	private Image dbImage;
	private int tileWidth;
	private int tileHeight;

	public GamePanel(GameEngine gameEngine, TilePainter tilePainter, CoinDisplay coinDisplay) {
		this.gameEngine = gameEngine;
		this.tilePainter = tilePainter;
		this.coinDisplay = coinDisplay;
		repaint();
	}

	public void init() {
		tileWidth = this.getWidth() / gameEngine.getLevelHorizontalDimension();
		tileHeight = this.getHeight() / gameEngine.getLevelVerticalDimension();
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		requestFocusInWindow();
		Point playerPosition = new Point(gameEngine.getPlayerXCoordinate(), gameEngine.getPlayerYCoordinate());
		Rectangle tileDimensions = new Rectangle(tileWidth, tileHeight);
		tilePainter.paintTiles(graphics, gameEngine, tileWidth, tileHeight);
		tilePainter.paintPlayer(graphics, playerPosition, tileDimensions, TileType.PLAYER);
		coinDisplay.draw(graphics);
	}

	@Override
	public void update(Graphics graphics) {
		if (dbImage == null) {
			dbImage = createImage(getWidth(), getHeight());
		}
		Graphics dbg = dbImage.getGraphics();
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, getWidth(), getHeight());
		dbg.setColor(getForeground());
		paint(dbg);
		graphics.drawImage(dbImage, 0, 0, this);
	}

	@Override
	public boolean keyDown(Event evt, int key) {
		if (key == Event.LEFT) {
			gameEngine.keyLeft();
		} else if (key == Event.RIGHT) {
			gameEngine.keyRight();
		} else if (key == Event.UP) {
			gameEngine.keyUp();
		} else if (key == Event.DOWN) {
			gameEngine.keyDown();
		}

		return true;
	}
}
