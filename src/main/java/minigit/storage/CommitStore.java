package minigit.storage;

import minigit.domain.Commit;
import minigit.domain.IndexEntry;
import minigit.util.Hashing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class CommitStore {
    private final Path commitsPath;

    public CommitStore(final Path repoPath) {
        this.commitsPath = repoPath.resolve("objects").resolve("commits");
    }

    public String saveCommit(Commit commit) {
        String content = formatCommit(commit);
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        String hash = Hashing.sha1(data);

        try{
            Files.write(commitsPath.resolve(hash), data);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save commit", e);
        }

        return hash;
    }

    public Commit readCommit(String commitId) {
        try {
            Path commitPath = commitsPath.resolve(commitId);

            List<String> content = Files.readAllLines(commitPath, StandardCharsets.UTF_8);

            return parseCommit(commitId, content);
        }catch (IOException e){
            throw new RuntimeException("Failed to read commit " + commitId, e);
        }
    }

    private String formatCommit(Commit commit) {
        StringBuilder content = new StringBuilder("message: " + commit.getMessage() + "\n"
                                                + "parentId: " + commit.getParentId() + "\n"
                                                + "files:\n");

        List<IndexEntry> indexEntries = commit.getIndexEntries();
        for (IndexEntry indexEntry : indexEntries) {
            content.append(formatEntry(indexEntry)).append("\n");
        }
        return content.toString();
    }

    private Commit parseCommit(String commitId, List<String> content) {
        String message = null;
        String parentId = null;
        List<IndexEntry> files = new ArrayList<>();
        boolean nextFileFlag = false;

        for (String line : content) {
            if (line.startsWith("message: ")) {
                message = line.substring("message: ".length()).trim();
            } else if (line.startsWith("parentId: ")) {
                parentId = parseParentId(line);
            } else if (line.startsWith("files:")) {
                nextFileFlag = true;
            } else if (nextFileFlag) {
                files.add(parseEntry(line));
            }
        }
        Commit commit = new Commit(message, parentId, files);
        commit.setId(commitId);
        return commit;
    }

    private String parseParentId(String line) {
        String parentId = line.substring("parentId: ".length()).trim();
        if (parentId.equals("null")) {
            return null;
        }
        return parentId;
    }

    private IndexEntry parseEntry(String line) {
        String[] fileLine = line.split("\\|");
        return new IndexEntry(Path.of(fileLine[0]), fileLine[1]);
    }

    private String formatEntry(IndexEntry indexEntry) {
        return indexEntry.getFilePath().toString() + "|" + indexEntry.getBlobId();
    }
}
