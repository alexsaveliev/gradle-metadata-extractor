package com.sourcegraph.gradle

public abstract class GradleScript extends Script {

    GradleScript() {
    }

    def Collection<String> getIncludes() {
        return getProject().getIncludes()
    }

    def call(arguments) {
        getProject().call(arguments)
    }

    def apply(Map arguments) {
        getProject().apply(arguments)
    }

    def repositories(Closure args) {
        getProject().repositories(args)
    }

    def dependencies(Closure args) {
        getProject().dependencies(args)
    }

    def subprojects(Closure args) {
        getProject().subprojects(args)
    }

    def allprojects(Closure args) {
        getProject().allprojects(args)
    }

    def methodMissing(String name, args) {
    }

    def propertyMissing(String name) {
        return new CatchAll()
    }

    private GradleProject getProject() {
        return (GradleProject) binding.getProperty("project")
    }

}
