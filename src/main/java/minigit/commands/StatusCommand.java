package minigit.commands;

import minigit.core.CommandRequest;
import minigit.core.RepositoryPaths;
import minigit.domain.IndexEntry;
import minigit.storage.IndexStore;
import minigit.util.Hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatusCommand implements Command {
    public void execute(CommandRequest request) {
        RepositoryPaths paths = RepositoryPaths.fromCurrentDirectory();
        paths.ensureInitialized();
        Path repoPath = paths.repoPath();
        Path workingDir = paths.workingDir();
        Set<String> trackedFiles = new HashSet<>();

        IndexStore indexStore = new IndexStore(repoPath);
        List<IndexEntry> index = indexStore.readIndex();

        System.out.println("Staged: ");
        for (IndexEntry staged : index) {
            System.out.println(staged.getFilePath());
            trackedFiles.add(staged.getFilePath().toString());
        }

        Set<String> modified = findModifiedFiles(index);
        System.out.println("\nModified: ");
        for (String modifiedPath : modified) {
            System.out.println(modifiedPath);
        }

        List<String> untrackedFiles = findUntrackedFiles(workingDir, trackedFiles);
        System.out.println("\nUntracked: ");
        for (String file : untrackedFiles) {
            System.out.println(file);
        }
    }

    private Set<String> findModifiedFiles(List<IndexEntry> index) {
        Set<String> modified = new HashSet<>();
        for (IndexEntry entry : index) {
            Path entryPath = entry.getFilePath();
            try {
                if (Files.notExists(entryPath)) {
                    modified.add(entryPath.toString());
                    continue;
                }
                byte[] data = Files.readAllBytes(entryPath);
                String entryHash = Hashing.sha1(data);
                if (!entryHash.equals(entry.getBlobId())) {
                    modified.add(entry.getFilePath().toString());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading file: " + entryPath, e);
            }
        }
        return modified;
    }

    private List<String> findUntrackedFiles(Path workingDir, Set<String> trackedFiles) {
        List<String> allFiles = collectFiles(workingDir);
        List<String> untrackedFiles = new ArrayList<>();

        for (String file : allFiles) {
            if (shouldIgnore(file)) {
                continue;
            }
            if (!trackedFiles.contains(file)) {
                untrackedFiles.add(file);
            }
        }
        return untrackedFiles;
    }

    private List<String> collectFiles(Path workingDir) {
        List<String> allFiles = new ArrayList<>();
        try {
            Files.walk(workingDir)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        Path relativePath = workingDir.relativize(file);
                        allFiles.add(relativePath.toString());
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error walking: " + workingDir, e);
        }
        return allFiles;
    }

    private boolean shouldIgnore(String file) {
        return file.startsWith(".minigit")
                || file.startsWith(".git")
                || file.startsWith("build")
                || file.startsWith(".gradle")
                || file.startsWith(".idea");
    }
}
