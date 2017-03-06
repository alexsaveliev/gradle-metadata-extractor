package com.sourcegraph.gradle

import org.apache.commons.lang3.StringUtils

class GradleDependencies {

    Collection<GradleDependency> dependencies

    GradleDependencies() {
        this.dependencies = new ArrayList<>()
    }

    def methodMissing(String name, args) {
        process(args)
    }

    private def process(Object arg) {
        if (arg instanceof Map) {
            add((Map) arg)
        } else if (arg instanceof String) {
            add((String) arg)
        } else if (arg instanceof Collection || arg instanceof Object[]) {
            for (Object a : arg) {
                process(a)
            }
        }
    }

    private def add(Map arguments) {
        def group = arguments.get("group")
        def name = arguments.get("name")
        def version = arguments.get("version")
        if (group == null && name == null && version == null) {
            return
        }
        dependencies.add(new GradleDependency(group, name, version))
    }

    private def add(String dependency) {
        String[] tokens = StringUtils.split(dependency, ':')
        if (tokens.length != 3) {
            return
        }
        dependencies.add(new GradleDependency(tokens[0],
                tokens[1],
                StringUtils.substringBefore(tokens[2], '@')))
    }

}
