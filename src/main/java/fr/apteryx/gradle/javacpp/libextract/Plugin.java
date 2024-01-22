package fr.apteryx.gradle.javacpp.libextract;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;

public class Plugin implements org.gradle.api.Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getPlugins().apply("org.bytedeco.gradle-javacpp-platform");
        ObjectFactory objects = project.getObjects();
        NamedDomainObjectContainer<LibraryExtraction> libraryExtractionContainer =
            objects.domainObjectContainer(LibraryExtraction.class, name -> objects.newInstance(LibraryExtraction.class, name));
        project.getExtensions().add("libraryExtractions", libraryExtractionContainer);

        libraryExtractionContainer.all(libraryExtraction -> {
            String sourceSet = libraryExtraction.getName();
            project.getTasks().register(sourceSet+"ExtractLibraries",
                ExtractLibraries.class,
                task -> {
                  task.getSourceSet().set(sourceSet);
                  task.getTargetDirectory().set(libraryExtraction.getTargetDirectory());
                  task.getClearTargetDirectory().set(libraryExtraction.getClearTargetDirectory());
                  task.getAdditionalClasses().set(libraryExtraction.getAdditionalClasses());
                });
        });
    }
}
