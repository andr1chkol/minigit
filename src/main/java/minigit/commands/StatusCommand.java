package minigit.commands;

import minigit.core.CommandRequest;
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
        Path repoPath = Path.of(System.getProperty("user.dir"), ".minigit");
        Path workingDir = Path.of(System.getProperty("user.dir"));
        Set<String> trackedFiles = new HashSet<>();

        if (Files.notExists(repoPath)) {
            throw new RuntimeException("Repository not initialized");
        }

        IndexStore indexStore = new IndexStore(repoPath);
        List<IndexEntry> index = indexStore.readIndex();

        System.out.println("Staged: ");
        for (IndexEntry staged : index) {
            System.out.println(staged.getFilePath());
            trackedFiles.add(staged.getFilePath().toString());
        }

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
                if (!entryHash.equals(entry.getBlobId())){
                    modified.add(entry.getFilePath().toString());
                }
            }
            catch (IOException e){
                throw new RuntimeException("Error reading file: " + entryPath, e);
            }
        }
        System.out.println("Modified: ");
        for (String modifiedPath : modified) {
            System.out.println(modifiedPath);
        }

        List<String> allFiles = new ArrayList<>();
        List<String> untrackedFiles = new ArrayList<>();
        try{
            Files.walk(workingDir)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        Path relativePath = workingDir.relativize(file);
                        allFiles.add(relativePath.toString());
                    });
        }catch (IOException e){
            throw new RuntimeException("Error walking: " + workingDir, e);
        }

        for (String file : allFiles) {
            if (file.startsWith(".minigit")
                    || file.startsWith(".git")
                    || file.startsWith("build")
                    || file.startsWith(".gradle")
                    || file.startsWith(".idea")) {
                continue;
            }
            if (!trackedFiles.contains(file)) {
                untrackedFiles.add(file);
            }
        }
        System.out.println("Untracked: ");
        for (String file : untrackedFiles) {
            System.out.println(file);
        }
    }
}