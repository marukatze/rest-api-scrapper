package core;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class DataWriterTest {

    private BlockingQueue<DataRecord> queue;

    @BeforeEach
    void setUp() throws IOException {
        queue = new LinkedBlockingQueue<>();
        Files.deleteIfExists(Paths.get("src\\main\\resources\\data.json"));
        Files.deleteIfExists(Paths.get("src\\main\\resources\\data.csv"));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("src\\main\\resources\\data.json"));
        Files.deleteIfExists(Paths.get("src\\main\\resources\\data.csv"));
    }

    @Test
    void writeJSON_shouldCreateValidJsonFile() throws Exception {
        DataRecord record = new DataRecord("meowfacts-api", "cat-fact", "Cats are cool!");
        queue.put(record);

        DataWriter writer = new DataWriter(queue, FileFormat.JSON);

        Thread t = new Thread(() -> {
            try {
                writer.write();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        t.join();

        writer.closeFile();

        String content = Files.readString(Paths.get("src\\main\\resources\\data.json"));
        assertTrue(content.contains("\"Cats are cool!\""));
        assertTrue(content.startsWith("["));
        assertTrue(content.endsWith("]\n"));
    }

    @Test
    void writeCSV_shouldCreateValidCsvFile() throws Exception {
        DataRecord record = new DataRecord("geek-jokes-api", "joke", "Programmers hate bugs");
        queue.put(record);

        DataWriter writer = new DataWriter(queue, FileFormat.CSV);

        Thread t = new Thread(() -> {
            try {
                writer.write();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        t.join();

        String content = Files.readString(Paths.get("src\\main\\resources\\data.csv"));
        assertTrue(content.contains("geek-jokes-api,joke,Programmers hate bugs"));
    }

    @Test
    void closeFile_shouldAppendClosingBracketToJson() throws Exception {
        Files.writeString(Paths.get("src\\main\\resources\\data.json"), "[{\"test\":true}");
        DataWriter writer = new DataWriter(queue, FileFormat.JSON);
        writer.closeFile();

        String content = Files.readString(Paths.get("src\\main\\resources\\data.json"));
        assertTrue(content.endsWith("]\n"), "File should end with a closing bracket and newline");
    }
}
