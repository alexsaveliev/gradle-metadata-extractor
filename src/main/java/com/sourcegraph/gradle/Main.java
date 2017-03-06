package com.sourcegraph.gradle;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class Main {

    private static String[] getGradleFiles() {
        File root = new File(System.getProperty("user.dir"));
        return root.list((dir, name) -> name.endsWith(".gradle"));
    }

    public static void main(String args[]) throws Exception {

        File root = new File(System.getProperty("user.dir"));
        MetadataExtractor metadataExtractor = new MetadataExtractor();

        for (String uri : getGradleFiles()) {
            File file = new File(root, uri);
            String source = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);
            metadataExtractor.process(uri, source);
        }

        System.out.println("group = " + metadataExtractor.getGroup());
        System.out.println("artifact = " + metadataExtractor.getName());
        System.out.println("version = " + metadataExtractor.getVersion());
        System.out.println("android = " + metadataExtractor.isAndroid());
        System.out.println("deps = " + metadataExtractor.getDependencies());
        System.out.println("repositories = " + metadataExtractor.getRepositories());

    }
}
