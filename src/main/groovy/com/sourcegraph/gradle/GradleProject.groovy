package com.sourcegraph.gradle

class GradleProject {

    private Binding binding
    private boolean _android
    private Collection<String> includes = new LinkedList<>()


    GradleProject(Binding binding) {
        this.binding = binding
    }

    def methodMissing(String name, args) {
    }

    public Object getVariable(String variable) {
        return binding.hasVariable(variable) ? binding.getVariable(variable) : null
    }

    public void setVariable(String variable, Object newValue) {
        binding.setVariable(variable, newValue)
    }

    public Object getProperty(String property) {
        return binding.getProperty(property)
    }

    public void setProperty(String property, Object newValue) {
        binding.setProperty(property, newValue)
    }

    def call(Object... arguments) {
        for (Object argument : arguments) {
            if (argument instanceof Closure) {
                argument.setDelegate(this)
                argument.setResolveStrategy(Closure.DELEGATE_FIRST)
                argument.call()
            }
        }
    }


    def apply(Map arguments) {
        if (arguments.containsKey("plugin")) {
            String id = arguments.get("plugin").toString()
            if ("com.android.application".equals(id) || "com.android.library".equals(id)) {
                _android = true
            }
        }
        if (arguments.containsKey("from")) {
            String id = arguments.get("from").toString()
            includes.add(id)
        }
    }

    def repositories(Closure args) {
        args.setDelegate(binding.getProperty("repositories"))
        args.setResolveStrategy(Closure.DELEGATE_FIRST)
        args.call()
    }

    def dependencies(Closure args) {
        args.setDelegate(binding.getProperty("dependencies"))
        args.setResolveStrategy(Closure.DELEGATE_FIRST)
        args.call()
    }

    def subprojects(Closure args) {
        args.setDelegate(this)
        args.setResolveStrategy(Closure.DELEGATE_FIRST)
        args.call()
    }

    def allprojects(Closure args) {
        args.setDelegate(this)
        args.setResolveStrategy(Closure.DELEGATE_FIRST)
        args.call()
    }

    def Collection<String> getIncludes() {
        return new LinkedList(includes)
    }
}
