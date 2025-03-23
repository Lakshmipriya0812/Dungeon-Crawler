package uitests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.WindowEvent;

import org.junit.jupiter.api.Test;

import engine.GameEngine;
import main.ObjectFactory;
import ui.GameFrame;

public class WindowClosingUITest {

	GameFrame gameFrame;
	GameEngine gameEngine;

	@Test
	public void window_closes_event() {
		gameEngine = ObjectFactory.getDefaultGameEngine();
		gameFrame = ObjectFactory.getDefaultGameFrame();
		gameFrame.dispatchEvent(new WindowEvent(gameFrame, WindowEvent.WINDOW_CLOSING));
		assertTrue(gameEngine.isExit());
	}

}
