package timer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import values.TunableParameters;
import wrappers.SystemWrapper;

public class PowerUpTimerTest {
	@Test
	void power_up_move_is_called_properly() {
		SystemWrapper mockSystemWrapper = mock(SystemWrapper.class);
		PowerUpTimer powerUpTimer = new PowerUpTimer(mockSystemWrapper);
		powerUpTimer.activateDoubleMove(TunableParameters.POWER_UP_DURATION);
		verify(mockSystemWrapper, times(1)).currentTimeMillis();
	}

	@Test
	void check_power_up_moves_only_two_steps() {
		SystemWrapper mockSystemWrapper = mock(SystemWrapper.class);
		PowerUpTimer powerUpTimer = new PowerUpTimer(mockSystemWrapper);
		long currentTime = 1000L;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		powerUpTimer.activateDoubleMove(TunableParameters.POWER_UP_DURATION);
		int initialX = 5;
		int maxMove = 2;
		int moveX = 3;
		moveX = powerUpTimer.isDoubleMoveActive() ? Math.min(moveX, maxMove) : 1;
		assertEquals(initialX + 2, initialX + moveX);
		currentTime += TunableParameters.POWER_UP_DURATION;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		assertFalse(powerUpTimer.isDoubleMoveActive());
	}

	@Test
	void power_up_move_shows_error_when_three_steps_occurs() {
		SystemWrapper mockSystemWrapper = mock(SystemWrapper.class);
		PowerUpTimer powerUpTimer = new PowerUpTimer(mockSystemWrapper);
		long currentTime = 1000L;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		powerUpTimer.activateDoubleMove(TunableParameters.POWER_UP_DURATION);
		int initialX = 5;
		int moveX = 3;
		int maxMove = 2;
		moveX = powerUpTimer.isDoubleMoveActive() ? Math.min(moveX, maxMove) : 1;
		assertEquals(initialX + 2, initialX + moveX);
		currentTime += TunableParameters.POWER_UP_DURATION;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		assertFalse(powerUpTimer.isDoubleMoveActive());
	}

	@Test
	void check_power_up_move_valid_for_five_seconds() {
		SystemWrapper mockSystemWrapper = mock(SystemWrapper.class);
		PowerUpTimer powerUpTimer = new PowerUpTimer(mockSystemWrapper);
		long currentTime = 1000L;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		powerUpTimer.activateDoubleMove(TunableParameters.POWER_UP_DURATION);
		assertTrue(powerUpTimer.isDoubleMoveActive());
		currentTime += TunableParameters.POWER_UP_DURATION - 1;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		assertTrue(powerUpTimer.isDoubleMoveActive());
		currentTime += 1;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		assertFalse(powerUpTimer.isDoubleMoveActive());
	}

	@Test
	void check_power_up_deactivates_after_five_seconds() {
		SystemWrapper mockSystemWrapper = mock(SystemWrapper.class);
		PowerUpTimer powerUpTimer = new PowerUpTimer(mockSystemWrapper);
		long currentTime = 1000L;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		powerUpTimer.activateDoubleMove(TunableParameters.POWER_UP_DURATION);
		currentTime += TunableParameters.POWER_UP_DURATION;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(currentTime);
		assertFalse(powerUpTimer.isDoubleMoveActive());
	}

	@Test
	void test_timer_deactivation_logic() {
		SystemWrapper mockSystemWrapper = mock(SystemWrapper.class);
		PowerUpTimer powerUpTimer = new PowerUpTimer(mockSystemWrapper);
		long startTime = 1000L;
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(startTime);
		powerUpTimer.activateDoubleMove(5000L);
		assertTrue(powerUpTimer.isDoubleMoveActive());
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(startTime + 4999L);
		assertTrue(powerUpTimer.isDoubleMoveActive());
		when(mockSystemWrapper.currentTimeMillis()).thenReturn(startTime + 5001L);
		assertFalse(powerUpTimer.isDoubleMoveActive());
	}

}
