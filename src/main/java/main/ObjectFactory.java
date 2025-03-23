package main;

import engine.GameEngine;
import parser.LevelCreator;
import timer.FramesPerSecondHandler;
import timer.PowerUpTimer;
import ui.*;
import values.TunableParameters;
import wrappers.ReaderWrapper;
import wrappers.SystemWrapper;
import wrappers.ThreadWrapper;

public abstract class ObjectFactory {
	private static ThreadWrapper defaultThreadWrapper = new ThreadWrapper();
	private static SystemWrapper systemWrapper = new SystemWrapper();
	private static LevelCreator defaultLevelCreator = new LevelCreator(TunableParameters.FILE_LOCATION_PREFIX,
			new ReaderWrapper());
	private static PowerUpTimer powerUpTimer = new PowerUpTimer(systemWrapper);
	private static GameEngine defaultGameEngine = new GameEngine(defaultLevelCreator, systemWrapper, powerUpTimer);
	private static CoinDisplay defaultCoinDisplay = new CoinDisplay(defaultGameEngine);
	private static GameFrame defaultGameFrame = new GameFrame(
			new GamePanel(defaultGameEngine, new TilePainter(), defaultCoinDisplay),
			new WindowAdapterSystemExit(defaultGameEngine));
	private static FramesPerSecondHandler defaultFramesPerSecondHandler = new FramesPerSecondHandler(
			TunableParameters.TARGET_FPS, new SystemWrapper());

	private ObjectFactory() {
	}

	public static ThreadWrapper getDefaultThreadWrapper() {
		return defaultThreadWrapper;
	}

	public static GameEngine getDefaultGameEngine() {
		return defaultGameEngine;
	}

	public static GameFrame getDefaultGameFrame() {
		return defaultGameFrame;
	}

	public static FramesPerSecondHandler getDefaultFramesPerSecondHandler() {
		return defaultFramesPerSecondHandler;
	}

}
