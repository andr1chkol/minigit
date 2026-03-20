package minigit.commands;

import minigit.core.CommandRequest;
import minigit.domain.IndexEntry;
import minigit.storage.IndexStore;
import minigit.storage.ObjectStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AddCommand implements Command {

    @Override
    public void execute(CommandRequest request) {
        if (request.getArgs().isEmpty()) {
            throw new RuntimeException("No arguments specified");
        }

        Path filePath = Path.of(request.getArgs().getFirst());
        if (Files.notExists(filePath)) {
            throw new RuntimeException("File " + filePath + " does not exist");
        }

        Path repoPath = Path.of(System.getProperty("user.dir"), ".minigit");
        if (Files.notExists(repoPath)) {
            throw new RuntimeException("Repository not initialized");
        }

        try {
            byte[] data = Files.readAllBytes(filePath);

            ObjectStore object = new ObjectStore(repoPath);
            String blobId = object.saveBlob(data);

            IndexStore index = new IndexStore(repoPath);
            List<IndexEntry> indexEntries = index.readIndex();

            boolean found = false;
            for(IndexEntry entry : indexEntries) {
                if(entry.getFilePath().toString().equals(filePath.toString())) {
                    entry.setBlobId(blobId);
                    found = true;
                }

            }

            if(!found) {
                indexEntries.add(new IndexEntry(filePath, blobId));
            }

            index.writeIndex(indexEntries);
        }
        catch (IOException e) {
            throw new RuntimeException("Error reading file " + filePath.toAbsolutePath());
        }
    }
}

