package core;

import exceptions.InvalidFileFormat;
import exceptions.InvalidSource;
import utils.SourceChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static int n;
    private static int t;
    private static final List<Source> sources = new ArrayList<>();
    private static FileFormat format;


    public static void main(String[] args) {
        try {
            parseArgs(args);
        } catch (InvalidFileFormat | InvalidSource | NumberFormatException e) {
            System.err.println(e);
            return;
        }

        BlockingQueue<DataRecord> queue = new LinkedBlockingQueue<>();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(n + 1);

        DataWriter writer = new DataWriter(queue, format);
        executor.submit(writer);

        for (Source source : sources) {
            APIPoller poller = new APIPoller(source, queue, t, executor);
            executor.submit(poller);
        }

        // graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            writer.closeFile();
            executor.shutdownNow();
            System.out.println("Executor stopped.");
        }));
    }

    private static void parseArgs(String[] args) throws InvalidFileFormat, NumberFormatException {
        List<String> params = new java.util.ArrayList<>(Arrays.stream(args).toList());
        n = Integer.parseInt(params.get(0));
        t = Integer.parseInt(params.get(1));
        params.removeFirst();
        params.removeFirst();

        if (params.getLast().toLowerCase().contains("json")) {
            format = FileFormat.JSON;
            params.removeLast();
        } else if (params.getLast().toLowerCase().contains("csv")) {
            format = FileFormat.CSV;
            params.removeLast();
        } else {
            format = null;
            throw new InvalidFileFormat("file format doesn't fit");
        }

        for (String param : params) {
            sources.add(SourceChecker.checkSource(param));
        }
    }
}

