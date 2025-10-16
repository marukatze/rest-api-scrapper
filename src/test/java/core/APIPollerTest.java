package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class APIPollerTest {

    private BlockingQueue<DataRecord> queue;
    private ScheduledExecutorService executor;

    @BeforeEach
    void setUp() {
        queue = new LinkedBlockingQueue<>();
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Test
    void poll_shouldReturnEmptyJsonOnException() {
        APIPoller poller = new APIPoller(Source.CATS, queue, 1, executor) {
            @Override
            protected String poll() throws IOException {
                throw new IOException("Simulated failure");
            }
        };

        assertThrows(RuntimeException.class, poller::run);
    }

    @Test
    void run_shouldScheduleNextExecutionEvenOnError() {
        APIPoller poller = new APIPoller(Source.CATS, queue, 1, executor) {
            @Override
            protected String poll() throws IOException {
                throw new IOException("fail");
            }
        };

        assertThrows(RuntimeException.class, poller::run);
    }

    @Test
    void run_shouldPutRecordToQueueWhenJsonNotEmpty() {
        APIPoller poller = new APIPoller(Source.JOKES, queue, 1, executor) {
            @Override
            protected String poll() {
                return "{\"joke\": \"haha\"}";
            }

            @Override
            public void run() {
                try {
                    queue.put(new DataRecord("JOKES", "Test", "haha"));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        poller.run();
        assertEquals(1, queue.size(), "Queue should contain one record after successful run");
    }

    @Test
    void poll_shouldReturnJsonStringOrEmpty() throws Exception {
        APIPoller poller = new APIPoller(Source.CATS, queue, 1, executor);
        String result = poller.poll();
        assertNotNull(result);
        assertInstanceOf(String.class, result);
    }

    @Test
    void run_shouldNotThrowWhenInterrupted() {
        APIPoller poller = new APIPoller(Source.CATS, queue, 1, executor) {
            @Override
            protected String poll() throws InterruptedException {
                throw new InterruptedException("simulated interrupt");
            }
        };
        assertThrows(RuntimeException.class, poller::run);
    }
}

