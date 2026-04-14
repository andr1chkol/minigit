package minigit.core;

import java.nio.file.Files;
import java.nio.file.Path;

public class RepositoryPaths {
    private final Path workingDir;
    private final Path repoPath;

    private RepositoryPaths(Path workingDir) {
        this.workingDir = workingDir;
        this.repoPath = workingDir.resolve(".minigit");
    }

    public static RepositoryPaths fromCurrentDirectory() {
        return new RepositoryPaths(Path.of(System.getProperty("user.dir")));
    }

    public Path workingDir() {
        return workingDir;
    }

    public Path repoPath() {
        return repoPath;
    }

    public Path objectsPath() {
        return repoPath.resolve("objects");
    }

    public Path blobsPath() {
        return objectsPath().resolve("blobs");
    }

    public Path commitsPath() {
        return objectsPath().resolve("commits");
    }

    public Path refsPath() {
        return repoPath.resolve("refs");
    }

    public Path headsPath() {
        return refsPath().resolve("heads");
    }

    public Path headPath() {
        return repoPath.resolve("HEAD");
    }

    public Path indexPath() {
        return repoPath.resolve("index");
    }

    public Path mainRefPath() {
        return headsPath().resolve("main");
    }

    public boolean isInitialized() {
        return Files.exists(repoPath);
    }

    public void ensureInitialized() {
        if (Files.notExists(repoPath)) {
            throw new RuntimeException("Repository not initialized");
        }
    }
}
