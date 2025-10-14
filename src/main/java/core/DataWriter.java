package core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class DataWriter implements Runnable {
    private final FileFormat format;
    private final BlockingQueue<DataRecord> records;
    private final ObjectMapper mapper = new ObjectMapper();

    public DataWriter(BlockingQueue<DataRecord> records, FileFormat format) {
        this.records = records;
        this.format = format;
    }

    public void write() throws InterruptedException, IOException {
        DataRecord currentRecord = records.take();

        switch (format) {
            case JSON -> writeJSON(currentRecord);
            case CSV -> writeCSV(currentRecord);
        }
    }

    private void writeJSON(DataRecord record) throws IOException {
        try (FileWriter writer = new FileWriter("data.json", true)) {
            String json = mapper.writeValueAsString(record);
            writer.write(json + System.lineSeparator());
        }
    }

    private void writeCSV(DataRecord record) throws IOException {
        try (FileWriter writer = new FileWriter("data.csv", true)) {

            String csvLine = String.join(",",
                    record.source(),
                    record.title(),
                    record.content(),
                    record.fetchedAt().toString());
            writer.write(csvLine + System.lineSeparator());
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                write();
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
