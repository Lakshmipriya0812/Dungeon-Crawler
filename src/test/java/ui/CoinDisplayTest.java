package ui;

import static org.mockito.Mockito.*;

import java.awt.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import engine.GameEngine;

class CoinDisplayTest {

	private CoinDisplay coinDisplay;
	private GameEngine mockGameEngine;
	private Graphics2D mockGraphics;

	@BeforeEach
	void setUp() {
		mockGameEngine = mock(GameEngine.class);
		coinDisplay = new CoinDisplay(mockGameEngine);
		mockGraphics = mock(Graphics2D.class);
	}

	@Test
	void to_check_coin_count_in_draw_method() {
		when(mockGameEngine.getCollectedCoins()).thenReturn(10);
		coinDisplay.draw(mockGraphics);
		verify(mockGameEngine, times(1)).getCollectedCoins();
	}

	@Test
    void to_check_correct_font_and_color_is_used() {
        when(mockGameEngine.getCollectedCoins()).thenReturn(0);
        coinDisplay.draw(mockGraphics);
		verify(mockGraphics).setColor(Color.BLACK);
    }

	@Test
	void to_check_correct_font_is_used() {
		when(mockGameEngine.getCollectedCoins()).thenReturn(5);
		coinDisplay.draw(mockGraphics);
		Font expectedFont = new Font("Arial", Font.BOLD, 20);
		verify(mockGraphics).setFont(expectedFont);
	}

	@Test
	void to_check_correct_string_is_used() {
		when(mockGameEngine.getCollectedCoins()).thenReturn(15);
		coinDisplay.draw(mockGraphics);
		verify(mockGraphics).drawString("Coins Collected: 15", 50, 80);
	}
}
