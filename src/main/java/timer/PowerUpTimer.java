package timer;

public class PowerUpTimer {
	private final TimeProvider timeProvider;
	private long doubleMoveEndTime = 0;

	public PowerUpTimer(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public void activateDoubleMove(long durationInMillis) {
		doubleMoveEndTime = timeProvider.currentTimeMillis() + durationInMillis;
	}

	public boolean isDoubleMoveActive() {
		long currentTime = timeProvider.currentTimeMillis();
		boolean isActive = currentTime < doubleMoveEndTime;
		return isActive;
	}
}
