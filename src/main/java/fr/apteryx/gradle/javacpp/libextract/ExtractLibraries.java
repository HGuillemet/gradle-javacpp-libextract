package fr.apteryx.gradle.javacpp.libextract;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.*;
import org.gradle.workers.WorkerExecutor;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class ExtractLibraries extends DefaultTask {

    @Input
    public abstract Property<String> getSourceSet();

    @Input
    @Optional
    public abstract Property<Boolean> getClearTargetDirectory();

    @Input
    @Optional
    public abstract SetProperty<String> getAdditionalClasses();

    @Classpath
    @InputFiles
    FileCollection getRuntimeClasspath() {
        SourceSetContainer ssc = (SourceSetContainer) getProject().getProperties().get("sourceSets");
        return ssc.getByName(getSourceSet().get()).getRuntimeClasspath();
    }

    @InputFiles
    FileCollection getClassFiles() {
        SourceSetContainer ssc = (SourceSetContainer) getProject().getProperties().get("sourceSets");
        FileCollection dirs = ssc.getByName(getSourceSet().get()).getOutput().getClassesDirs();
        return dirs.getAsFileTree();
    }

    @OutputDirectory
    abstract public DirectoryProperty getTargetDirectory();

    @Inject
    abstract public WorkerExecutor getWorkerExecutor();

    @TaskAction
    void impl() throws IOException {
        File targetDir = getTargetDirectory().get().getAsFile();
        Path targetPath = targetDir.toPath();

        Files.createDirectories(targetPath);
        if (getClearTargetDirectory().getOrElse(true))
            clearDirectory(targetPath);
        System.err.println(getRuntimeClasspath().getAsPath());

        BytecodeAnalyzer bca = new BytecodeAnalyzer(getRuntimeClasspath());

        List<String> deps = bca.getJavaCPPDependencies(getClassFiles(), getAdditionalClasses().get());

        bca.loadClasses(deps, targetPath, getLogger());
        Files.deleteIfExists(targetPath.resolve(".lock"));
    }

    private void clearDirectory(Path path) throws IOException {
        Files.list(path).forEach(p -> {
            if (Files.isDirectory(p))
                throw new GradleException("Unexpected directory " + p);
            try {
                Files.delete(p);
            } catch (IOException e) {
                e.printStackTrace();
                throw new GradleException("Cannot delete file ", e);
            }
        });
    }

}
