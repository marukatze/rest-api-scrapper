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
    private static List<Source> sources;
    private static FileFormat format;


    public static void main(String[] args) {
        try {
            parseArgs(args);
        } catch (InvalidFileFormat | InvalidSource | NumberFormatException e) {
            System.err.println(e);
        }

        BlockingQueue<DataRecord> queue = new LinkedBlockingQueue<>();
        List<Runnable> tasks = new ArrayList<>();
        for (Source source : sources) {
            tasks.add(new APIPoller(source, queue, t));
        }

        try (ExecutorService pollers = Executors.newFixedThreadPool(n);
             ExecutorService dataWriter = Executors.newSingleThreadExecutor()) {

            tasks.forEach(pollers::submit);
            datawriter.submit(new DataWriter(queue, format));
        }


    }


    private static void parseArgs(String[] args) throws InvalidFileFormat, NumberFormatException {
        List<String> params = new java.util.ArrayList<>(Arrays.stream(args).toList());
        n = Integer.parseInt(params.getFirst());
        params.removeFirst();
        t = Integer.parseInt(params.get(1));
        params.remove(1);

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
            try {
                sources.add(SourceChecker.checkSource(param));
            } catch (InvalidSource e) {
                System.err.println(e);
            }
        }
    }
}

