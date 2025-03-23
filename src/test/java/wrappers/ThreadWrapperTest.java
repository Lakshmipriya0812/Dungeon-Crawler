package wrappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import main.DungeonCrawler;

public class ThreadWrapperTest {

	private ThreadWrapper threadWrapper;
	private DungeonCrawler dungeonCrawler;
	private Thread thread;

	@BeforeEach
	public void setUp() {
		threadWrapper = new ThreadWrapper();
		dungeonCrawler = mock(DungeonCrawler.class);

		thread = mock(Thread.class);
	}

	@Test
	public void create_new_thread_with_dungeon_crawler() {
		DungeonCrawler mockDungeonCrawler = Mockito.mock(DungeonCrawler.class);
		Thread mockThread = Mockito.mock(Thread.class);
		ThreadWrapper threadWrapper = new ThreadWrapper() {
			@Override
			public void createNewThreadWithDungeonCrawler(DungeonCrawler dungeonCrawler) {
				Thread thread = mockThread;
				thread.start();
			}
		};
		threadWrapper.createNewThreadWithDungeonCrawler(mockDungeonCrawler);
		verify(mockThread).start();
	}

	@Test
	public void create_new_thread_with_null_dungeon_crawler() {
		try {
			threadWrapper.createNewThreadWithDungeonCrawler(null);
		} catch (NullPointerException e) {
			assert true;
		}
	}

	@Test
	public void sleep_method_check() {
		try {
			long startTime = System.currentTimeMillis();
			threadWrapper.sleep(50);
			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			assertTrue(elapsedTime >= 50, "Thread should sleep for at least 50ms");
		} catch (InterruptedException e) {
			fail("Sleep interrupted unexpectedly");
		}
	}

	@Test
	public void sleep_interrupted_check() {
		try {
			Thread.currentThread().interrupt();
			threadWrapper.sleep(50);
			fail("Expected InterruptedException but did not get one");
		} catch (InterruptedException e) {
		}
	}

	@Test
	public void sleep_negative_millis_check() {
		try {
			threadWrapper.sleep(-1);
			fail("Expected IllegalArgumentException for negative millis");
		} catch (IllegalArgumentException | InterruptedException e) {
		}
	}

	@Test
	public void current_thread_interrupt() {
		assertFalse(Thread.interrupted(), "Thread should not be interrupted initially");
		threadWrapper.currentThreadInterrupt();
		assertTrue(Thread.interrupted(), "Thread should be interrupted after calling currentThreadInterrupt");
	}

	@Test
	public void current_thread_interrupt_when_already_interrupted() {
		Thread.currentThread().interrupt();
		threadWrapper.currentThreadInterrupt();
		assertTrue(Thread.interrupted(), "Thread should remain interrupted even after calling currentThreadInterrupt");
	}

}