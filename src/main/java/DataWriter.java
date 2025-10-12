import java.util.concurrent.BlockingQueue;

public class DataWriter implements Runnable {
    FileFormat format;
    BlockingQueue<DataRecord> records;

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
