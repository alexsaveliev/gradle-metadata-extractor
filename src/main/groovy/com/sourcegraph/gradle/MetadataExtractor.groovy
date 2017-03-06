package com.sourcegraph.gradle

import org.apache.commons.io.FileUtils
import org.codehaus.groovy.control.CompilerConfiguration

public class MetadataExtractor {

    private GradleScript script
    private GradleProject project
    private GroovyShell shell

    MetadataExtractor() {
        Binding binding = new Binding()
        binding.setVariable("dependencies", new GradleDependencies())
        binding.setVariable("repositories", new GradleRepositories())

        project = new GradleProject(binding)
        // populating base Gradle properties
        binding.setVariable("project", project)
        binding.setVariable("rootProject", project)
        binding.setVariable("allprojects", project)
        binding.setVariable("subprojects", project)

        File buildDir = FileUtils.getTempDirectory()
        binding.setVariable("buildDir", buildDir.toString())
        binding.setVariable("projectDir", buildDir.toString())
        binding.setVariable("rootDir", buildDir.toString())

        def config = new CompilerConfiguration()
        config.scriptBaseClass = GradleScript.class.name
        shell = new GroovyShell(this.class.classLoader, binding, config)
    }

    def process(String path, String source) {
        project.setVariable("buildFile", new File(FileUtils.getTempDirectory(), path).toString())
        this.script = (GradleScript) shell.parse(source)
        this.script.run()
    }

    def String getVersion() {
        return project.getVariable("version")
    }

    def String getName() {
        return project.getVariable("name")
    }

    def String getGroup() {
        return project.getVariable("group")
    }

    def boolean isAndroid() {
        return script != null && script._android
    }

    def Collection<GradleDependency> getDependencies() {
        return ((GradleDependencies) project.getVariable("dependencies")).dependencies
    }

    def Collection<String> getRepositories() {
        return ((GradleRepositories) project.getVariable("repositories")).uris
    }

    def Collection<String> getIncludes() {
        return script.getIncludes()
    }

}

