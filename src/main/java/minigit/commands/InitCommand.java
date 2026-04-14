package minigit.commands;

import minigit.core.CommandRequest;
import minigit.core.RepositoryPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InitCommand implements Command {
    @Override
    public void execute(CommandRequest request) {
        RepositoryPaths paths = RepositoryPaths.fromCurrentDirectory();

        final Path repoPath = paths.repoPath();
        final Path blobsPath = paths.blobsPath();
        final Path commitsPath = paths.commitsPath();
        final Path headsPath = paths.headsPath();
        final Path headFile = paths.headPath();
        final Path indexFile = paths.indexPath();
        final Path mainRef = paths.mainRefPath();

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
