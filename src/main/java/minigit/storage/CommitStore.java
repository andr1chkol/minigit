package minigit.storage;

import minigit.domain.Commit;
import minigit.domain.IndexEntry;
import minigit.util.Hashing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class CommitStore {
    private final Path commitsPath;

    public CommitStore(final Path repoPath) {
        this.commitsPath = repoPath.resolve("objects").resolve("commits");
    }

    public String saveCommit(Commit commit) {
        StringBuilder content = new StringBuilder("message: " + commit.getMessage() + "\n"
                                                + "parentId: " + commit.getParentId() + "\n"
                                                + "files:\n");

        List<IndexEntry> indexEntries = commit.getIndexEntries();
        for(IndexEntry indexEntry : indexEntries){
            content.append(indexEntry.getFilePath().toString()).
                    append("|").
                    append(indexEntry.getBlobId()).
                    append("\n");
        }

        byte[] data = content.toString().getBytes(StandardCharsets.UTF_8);
        String hash = Hashing.sha1(data);

        try{
            Files.write(commitsPath.resolve(hash), data);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save commit", e);
        }

        return hash;
    }
}