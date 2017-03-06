package com.sourcegraph.gradle

class GradleRepositories {

    Collection<String> uris

    GradleRepositories() {
        this.uris = new ArrayList<>()
    }

    def maven(Closure args) {
        args.setDelegate(this)
        args.setResolveStrategy(Closure.DELEGATE_FIRST)
        args.call()
    }

    def mavenCentral(Object... args) {
        uris.add("https://repo1.maven.org/maven2")
    }

    def jcenter(Object... args) {
        uris.add("https://jcenter.bintray.com")
    }

    def url(Object... args) {
        for (Object arg : args) {
            if (arg instanceof String) {
                uris.add(arg)
            }
        }
    }

    def methodMissing(String name, args) {
    }
}
