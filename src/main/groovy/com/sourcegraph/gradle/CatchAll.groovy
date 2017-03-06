package com.sourcegraph.gradle

class CatchAll {

    CatchAll() {
    }

    def methodMissing(String name, args) {
        return this
    }

    def propertyMissing(String name) {
        return this
    }

    def propertyMissing(String name, Object v) {
        return this
    }

}
