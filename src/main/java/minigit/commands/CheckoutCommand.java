package minigit.commands;

import minigit.core.CommandRequest;
import minigit.core.RepositoryPaths;
import minigit.domain.Commit;
import minigit.domain.IndexEntry;
import minigit.storage.CommitStore;
import minigit.storage.ObjectStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CheckoutCommand implements Command {

    public void execute(CommandRequest request) {
        if (request.getArgs().isEmpty()) {
            throw new RuntimeException("No arguments specified");
        }

        RepositoryPaths paths = RepositoryPaths.fromCurrentDirectory();
        paths.ensureInitialized();
        Path repoPath = paths.repoPath();

        String commitId = request.getArgs().getFirst();
        CommitStore commitStore = new CommitStore(repoPath);
        Commit commit = commitStore.readCommit(commitId);
        List<IndexEntry> indexEntries = commit.getIndexEntries();

        ObjectStore objectStore = new ObjectStore(repoPath);
        for (IndexEntry indexEntry : indexEntries) {
            byte[] data = objectStore.readBlob(indexEntry.getBlobId());

            Path dataPath = paths.workingDir().resolve(indexEntry.getFilePath());


            try{
                Path parent = dataPath.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.write(dataPath, data);
            }
            catch(IOException e){
                throw new RuntimeException("Failed to restore file: " + dataPath, e);
            }
        }
        System.out.println("Checked out " + commitId);
    }
}
