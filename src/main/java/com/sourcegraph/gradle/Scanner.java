package com.sourcegraph.gradle;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class Scanner {

    private static String[] getGradleFiles(File root) {
        return root.list((dir, name) -> name.endsWith(".gradle"));
    }

    private static File[] getDirectories(File root) {
        return root.listFiles((dir, name) -> !name.startsWith(".") && new File(root, name).isDirectory());
    }

    private static void process(File root) {
        MetadataExtractor metadataExtractor = new MetadataExtractor();
        File current = null;
        try {
            for (String uri : getGradleFiles(root)) {
                current = new File(root, uri);
                String source = IOUtils.toString(new FileInputStream(current), StandardCharsets.UTF_8);
                metadataExtractor.process(uri, source);
            }
            // touch properties
            metadataExtractor.getGroup();
            metadataExtractor.getName();
            metadataExtractor.getVersion();
            metadataExtractor.isAndroid();
            metadataExtractor.getDependencies();
            metadataExtractor.getRepositories();
        } catch (Exception e) {
            System.err.println(current);
            e.printStackTrace();

        }
        for (File child : getDirectories(root)) {
            process(child);
        }

    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            System.err.println("Usage: Scanner <ROOT-DIRECTORY>");
            System.exit(1);
        }

        process(new File(args[0]));
    }
}
