package minigit.commands;

import minigit.core.CommandRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InitCommand implements Command {
    @Override
    public void execute(CommandRequest request) {

        final Path repoPath = Path.of(System.getProperty("user.dir"), ".minigit");

        final Path objectsPath = repoPath.resolve("objects");
        final Path blobsPath = objectsPath.resolve("blobs");
        final Path commitsPath = objectsPath.resolve("commits");

        final Path refsPath = repoPath.resolve("refs");
        final Path headsPath = refsPath.resolve("heads");

        final Path headFile = repoPath.resolve("HEAD");
        final Path indexFile = repoPath.resolve("index");
        final Path mainRef = headsPath.resolve("main");

        if (Files.exists(repoPath)) {
            System.out.println("MiniGit repository already initialized");
            return;
        }

        try {
            Files.createDirectories(blobsPath);
            Files.createDirectories(commitsPath);
            Files.createDirectories(headsPath);

            Files.createFile(headFile);
            Files.createFile(indexFile);
            Files.createFile(mainRef);

            Files.writeString(headFile, "ref: refs/heads/main");

            System.out.println("Initialized MiniGit repository");

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize repository", e);
        }
    }
}
