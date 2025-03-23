package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import engine.GameEngine;
import tiles.TileType;
import values.TunableParameters;
import wrappers.ReaderWrapper;

public class LevelCreator {
	private static final Logger LOGGER = Logger.getLogger(LevelCreator.class.getName());

	String fileLocationPrefix;
	String fileNameSuffix = TunableParameters.FILE_NAME_SUFFIX;
	ReaderWrapper readerWrapper;

	public LevelCreator(String fileLocationPrefix, ReaderWrapper readerWrapper) {
		this.fileLocationPrefix = fileLocationPrefix;
		this.readerWrapper = readerWrapper;
	}

	public void createLevel(GameEngine gameEngine, int level) {
		try (BufferedReader reader = createReaderForLevel(level)) {
			processLevel(reader, gameEngine);
		} catch (FileNotFoundException e) {
			handleFileNotFound(e, gameEngine);
		} catch (IOException e) {
			handleIOException(e, gameEngine);
		}
	}

	private BufferedReader createReaderForLevel(int level) throws FileNotFoundException {
		return readerWrapper.createBufferedReader(getFilePath(level));
	}

	private void processLevel(BufferedReader reader, GameEngine gameEngine) throws IOException {
		String line;
		int y = 0;
		int x = 0;
		while ((line = reader.readLine()) != null) {
			x = processLine(line, gameEngine, y);
			y++;
		}
		gameEngine.setLevelHorizontalDimension(x);
		gameEngine.setLevelVerticalDimension(y);
		gameEngine.addRandomCoins(10);
		gameEngine.addRandomPowerUps(5);
	}

	private int processLine(String line, GameEngine gameEngine, int y) {
		int x = 0;
		for (char ch : line.toCharArray()) {
			gameEngine.addTile(x, y, TileType.getTileTypeByChar(ch));
			x++;
		}
		return x;
	}

	private void handleFileNotFound(FileNotFoundException e, GameEngine gameEngine) {
		LOGGER.log(Level.SEVERE, e.toString(), e);
		gameEngine.setExit(true);
	}

	private void handleIOException(IOException e, GameEngine gameEngine) {
		LOGGER.log(Level.SEVERE, e.toString(), e);
		gameEngine.setExit(true);
	}

	String getFilePath(int level) {
		return fileLocationPrefix + level + fileNameSuffix;
	}
}
