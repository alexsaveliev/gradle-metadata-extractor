package com.sourcegraph.gradle

public class GradleDependency {

    String group
    String name
    String version

    GradleDependency(group, name, version) {
        this.name = name == null ? null : name.toString()
        this.group = group == null ? null : group.toString()
        this.version = version == null ? null : version.toString()
    }

    @Override
    def String toString() {
        return group + ':' + name + ':' + version
    }
}
