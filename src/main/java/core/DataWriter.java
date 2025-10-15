package core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

public class DataWriter implements Runnable {
    private final FileFormat format;
    private final BlockingQueue<DataRecord> records;
    private final ObjectMapper mapper = new ObjectMapper();
    private boolean firstRecord = true;
    private volatile boolean closed = false;

    public DataWriter(BlockingQueue<DataRecord> records, FileFormat format) {
        this.records = records;
        this.format = format;
    }

    public void write() throws InterruptedException, IOException {
        DataRecord currentRecord = records.take();
        System.out.println("current record to write: " + currentRecord);

        switch (format) {
            case JSON -> writeJSON(currentRecord);
            case CSV -> writeCSV(currentRecord);
        }
    }

    private void writeJSON(DataRecord record) throws IOException {
        try (FileWriter writer = new FileWriter("data.json", true)) {
            if (firstRecord) {
                writer.write("[\n");
                firstRecord = false;
            } else {
                writer.write(",\n");
            }
            String json = mapper.writeValueAsString(record);
            writer.write(json);
        }
    }

    private void writeCSV(DataRecord record) throws IOException {
        try (FileWriter writer = new FileWriter("data.csv", true)) {

            String csvLine = String.join(",",
                    record.source(),
                    record.title(),
                    record.content());
            writer.write(csvLine + System.lineSeparator());
        }
    }

    @Override
    public void run() {
        try {
            Files.deleteIfExists(Paths.get("data.json"));
            Files.deleteIfExists(Paths.get("data.csv"));
            while (!Thread.currentThread().isInterrupted()) {
                write();
                System.out.println("record is written correctly");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            closeFile();
        }
    }

    public void closeFile() {
        if (closed) return;
        closed = true;
        try (FileWriter writer = new FileWriter("data.json", true)) {
            writer.write("\n]\n");
            System.out.println("JSON file closed properly.");
        } catch (IOException e) {
            System.err.println("Error while closing JSON file: " + e.getMessage());
        }
    }
}
