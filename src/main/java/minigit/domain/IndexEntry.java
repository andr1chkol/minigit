package minigit.domain;

import java.nio.file.Path;

public class IndexEntry {
    private final Path filePath;
    private  String blobId;

    public IndexEntry(Path filePath, String blobId) {
        this.filePath = filePath;
        this.blobId = blobId;
    }

    public Path getFilePath() {
        return filePath;
    }
    public String getBlobId() {
        return blobId;
    }

    public void setBlobId(String blobId) {
        this.blobId = blobId;
    }
}
