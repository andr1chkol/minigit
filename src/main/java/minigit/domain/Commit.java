package minigit.domain;

import java.util.ArrayList;
import java.util.List;

public class Commit {
    private String id;
    private String message;
    private String parentId;
    private final List<IndexEntry> indexEntries;

    public Commit(String message, String parentId, List<IndexEntry> entries){
        this.message = message;
        this.parentId = parentId;
        this.indexEntries = entries;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getParentId() {
        return parentId;
    }

    public List<IndexEntry> getIndexEntries() {
        return indexEntries;
    }
}
