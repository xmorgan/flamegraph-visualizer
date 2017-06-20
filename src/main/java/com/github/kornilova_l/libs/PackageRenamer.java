package com.github.kornilova_l.libs;

import java.io.*;

public class PackageRenamer {
    private static final String outputDirPath = "/home/lk/java-profiling-plugin/src/main/java/com/github/kornilova_l/libs/protobuf/";
    private static final String oldPackageName = "com.google.protobuf";
    private static final String newPackageName = "com.github.kornilova_l.libs.protobuf";


    public static void main(String[] args) throws FileNotFoundException {
        String srcDirPath = "/home/lk/resources/protobuf/java/core/src/main/java/com/google/protobuf";

        File srcDir = new File(srcDirPath);
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            throw new AssertionError("invalid src directory");
        }
        processFiles(srcDir.listFiles());
    }

    private static void processFiles(File[] files) throws FileNotFoundException {
        if (files == null) {
            throw new IllegalArgumentException("Directory is empty");
        }
        for (File file : files) {
            try (
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(file)
                            )
                    );
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(
                                            new File(outputDirPath + file.getName())
                                    )
                            )
                    )
            ) {
                String line = reader.readLine();
                while (line != null) {
                    writer.write(line.replace(oldPackageName, newPackageName) + "\n");
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
