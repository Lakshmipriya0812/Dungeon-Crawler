package wrappers;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SystemWrapperTest {

	@Test
	public void nano_time() {
		SystemWrapper systemWrapper = new SystemWrapper();
		long time1 = systemWrapper.nanoTime();
		long time2 = systemWrapper.nanoTime();

		assertTrue(time1 > 0, "nanoTime should return a non-zero value");

		assertNotEquals(time1, time2, "nanoTime should return a different value on each call");
	}

	@Test
	public void nano_time_does_not_exceed_max_long_value() {
		SystemWrapper systemWrapper = new SystemWrapper();
		long time1 = systemWrapper.nanoTime();
		long time2 = systemWrapper.nanoTime();

		assertTrue(time1 < Long.MAX_VALUE, "nanoTime returned a value greater than Long.MAX_VALUE");
		assertTrue(time2 < Long.MAX_VALUE, "nanoTime returned a value greater than Long.MAX_VALUE");
	}

	@Test
	public void current_time_millis() {
		SystemWrapper systemWrapper = new SystemWrapper();
		long time1 = systemWrapper.currentTimeMillis();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long time2 = systemWrapper.currentTimeMillis();
		assertTrue(time1 > 0, "currentTimeMillis should return a non-zero value");
		assertNotEquals(time1, time2, "currentTimeMillis should return a different value on each call");
	}

	@Test
	public void current_time_millis_does_not_exceed_max_long_value() {
		SystemWrapper systemWrapper = new SystemWrapper();
		long time1 = systemWrapper.currentTimeMillis();
		long time2 = systemWrapper.currentTimeMillis();
		assertTrue(time1 < Long.MAX_VALUE, "currentTimeMillis returned a value greater than Long.MAX_VALUE");
		assertTrue(time2 < Long.MAX_VALUE, "currentTimeMillis returned a value greater than Long.MAX_VALUE");
	}

	@Test
	public void current_time_millis_is_increasing() {
		SystemWrapper systemWrapper = new SystemWrapper();
		long time1 = systemWrapper.currentTimeMillis();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long time2 = systemWrapper.currentTimeMillis();
		assertTrue(time2 > time1, "currentTimeMillis should increase over time");
	}
}
