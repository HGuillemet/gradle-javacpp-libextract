package fr.apteryx.gradle.javacpp.libextract;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;

public abstract class LibraryExtraction {
    private final String name;

    @javax.inject.Inject
    public LibraryExtraction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract public Property<Boolean> getClearTargetDirectory();

    abstract public DirectoryProperty getTargetDirectory();
}
