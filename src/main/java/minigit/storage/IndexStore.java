package minigit.storage;

import minigit.domain.IndexEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class IndexStore {
    private final Path indexPath;

    public IndexStore(final Path repoPath) {
        this.indexPath = repoPath.resolve("index");
    }

    public List<IndexEntry> readIndex(){
        List<IndexEntry> indexEntries = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(indexPath);
            for(String line : lines){
                String[] substr = line.split("\\|");
                indexEntries.add(new IndexEntry(Path.of(substr[0]), substr[1]));
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read index", e);
        }
        return  indexEntries;
    }

    public void writeIndex(List<IndexEntry> indexEntries){
        List<String> lines = new ArrayList<>();

        try{
            for(IndexEntry indexEntry : indexEntries){
                lines.add(indexEntry.getFilePath().toString() + "|" + indexEntry.getBlobId());
            }
            Files.write(indexPath, lines);
        }

        catch (IOException e) {
            throw new RuntimeException("Failed to write index", e);
        }
    }
}
