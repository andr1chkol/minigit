package minigit.commands;

import minigit.core.CommandRequest;
import minigit.domain.Commit;
import minigit.domain.IndexEntry;
import minigit.storage.CommitStore;
import minigit.storage.IndexStore;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CommitCommand implements Command {
    public void execute(CommandRequest request) {

        if (request.getArgs().isEmpty()) {
            throw new RuntimeException("No message specified");
        }

        Path repoPath = Path.of(System.getProperty("user.dir"), ".minigit");
        if (Files.notExists(repoPath)) {
            throw new RuntimeException("Repository not initialized");
        }

        try {
            IndexStore index = new IndexStore(repoPath);
            List<IndexEntry> indexEntries = index.readIndex();

            if(indexEntries.isEmpty()){
                throw new RuntimeException("Nothing to commit");
            }
            String parentId;
            Path mainRef = repoPath.resolve("refs").resolve("heads").resolve("main");

            List<String> mainLine = Files.readAllLines(mainRef);
            if(mainLine.isEmpty()){
                parentId = null;
            }
            else {
                parentId = mainLine.getFirst();
            }

            Commit commit = new Commit(request.getArgs().getFirst(), parentId, indexEntries);
            CommitStore commitStore = new CommitStore(repoPath);
            String commitId = commitStore.saveCommit(commit);

            Files.writeString(mainRef, commitId);
            index.writeIndex(List.of());
            System.out.println("Committed as " + commitId);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create commit", e);
        }
    }
}
