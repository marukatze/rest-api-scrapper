package core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.BlockingQueue;

public class DataWriter implements Runnable {
    private final FileFormat format;
    private final BlockingQueue<DataRecord> records;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DataWriter(BlockingQueue<DataRecord> records, FileFormat format) {
        this.records = records;
        this.format = format;
    }

    public void write() {

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            write();
        }
    }
}
