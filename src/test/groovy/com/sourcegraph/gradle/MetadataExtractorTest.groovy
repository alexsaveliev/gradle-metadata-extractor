package com.sourcegraph.gradle

import com.google.common.collect.Lists
import org.apache.commons.io.IOUtils
import org.junit.Test

import java.nio.charset.StandardCharsets
import static org.junit.Assert.*

class MetadataExtractorTest {

    @Test
    void testRepositories() throws Exception {
        MetadataExtractor result = extract("repositories")
        assertEquals(new HashSet<>(Lists.asList(
                "https://repo1.maven.org/maven2",
                "http://foo.bar",
                "http://baz.qux",
                "https://jcenter.bintray.com")),
                new HashSet<>(result.getRepositories()))
    }

    @Test
    void testDependencies() throws Exception {
        MetadataExtractor result = extract("dependencies")
        compareDependencies(Lists.asList(
                "a:b:c",
                "a:b:d",
                "a:b:e",
                "a:b:f",
                "a:b:g",
                "a:b:h",
                "a:b:i",
                "a:b:j",
                "a:b:k",
                "a:b:l",
                "a:b:m",
                "a:b:n",
                "null:f:null"
        ), result)
    }

    @Test
    void testMockito() throws Exception {
        MetadataExtractor result = extract("mockito")
        result.group
    }

    /*
    @Test
    void testSecurity() throws Exception {
        MetadataExtractor result = extract("security")
        result.group
    }
    */

    @Test
    void testAndroid() throws Exception {
        MetadataExtractor result = extract("android")
        assertTrue(result.isAndroid())
    }

    @Test
    void testProjectByNameRef() throws Exception {
        MetadataExtractor result = extract("project-by-name")
        assertEquals("org", result.getGroup())
        assertEquals("1.0", result.getVersion())
        compareDependencies(Lists.asList(
                "log4j:log4j:1.2.4",
                "junit:junit:4.0",
                "org:c:1.0"
        ), result)
    }

    private MetadataExtractor extract(String dir) {
        MetadataExtractor extractor = new MetadataExtractor()
        File root = new File(getClass().getClassLoader().getResource(dir).toURI())
        for (String file : getGradleFiles(root)) {
            extractor.process(file, IOUtils.toString(new FileInputStream(new File(root, file)), StandardCharsets.UTF_8))
        }
        return extractor
    }

    private static String[] getGradleFiles(File root) {
        return root.list(new FilenameFilter() {
            @Override
            boolean accept(File directory, String name) {
                return name.endsWith(".gradle") && new File(directory, name).isFile()
            }
        });
    }

    private static void compareDependencies(Collection<String> expected, MetadataExtractor result) {
        Collection<String> actual = new HashSet<>()
        for (GradleDependency dependency : result.getDependencies()) {
            actual.add(dependency.toString())
        }
        assertEquals(new HashSet<>(expected), actual)
    }

}
