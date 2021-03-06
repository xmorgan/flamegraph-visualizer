package com.github.kornilova_l.flamegraph.plugin.server.trees.generate_test_data;

import com.github.kornilova_l.flamegraph.javaagent.AgentFileManager;
import com.github.kornilova_l.flamegraph.javaagent.logger.Logger;
import com.github.kornilova_l.flamegraph.javaagent.logger.LoggerQueue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class TestHelper {
    private static Path path = Paths.get("src", "test", "resources", "out");

    public static void generateSerFile(Runnable runnable, String fileName) {
//        LoggerQueue.initLoggerQueue();
//        Logger logger = new Logger();
//
//        startLogger(logger);
//
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            logger.finish();
//            File file = getLatestFile();
//            TestHelper.renameFile(file, fileName + ".ser");
//        }));
//
//        runnable.run();

    }

    private static void startLogger(Logger logger) {
        Thread loggerThread = new Thread(logger, "logging thread");
        loggerThread.setDaemon(true);
        loggerThread.start();
        System.out.println("new thread " + loggerThread.getId());
    }

    private static File getLatestFile() {
        File[] files = new File(path.toString()).listFiles();
        assert files != null;
        Optional<File> latestFile = Arrays.stream(files)
                .max(Comparator.comparingLong(File::lastModified));
        assert latestFile.isPresent();
        return latestFile.get();
    }

    private static void renameFile(File file, String newName) {
        Path path = Paths.get(file.getAbsolutePath());
        try {
            Path siblingPath = path.resolveSibling(newName);
            File siblingFile = new File(siblingPath.toString());
            //noinspection RedundantIfStatement
            if (siblingFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                siblingFile.delete();
            }
            Files.move(path, siblingPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
