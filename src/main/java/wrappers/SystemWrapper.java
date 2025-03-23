package wrappers;

import timer.TimeProvider;

public class SystemWrapper implements TimeProvider {

	public long nanoTime() {
		return System.nanoTime();
	}

	@Override
	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}
}
