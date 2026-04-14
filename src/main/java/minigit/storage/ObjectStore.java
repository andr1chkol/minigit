package minigit.storage;

import minigit.util.Hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ObjectStore {
    private final Path repoPath;

    public ObjectStore(final Path repoPath) {
        this.repoPath = repoPath;
    }

    public String saveBlob(byte[] data){
        String hash = Hashing.sha1(data);

        Path blobPath = repoPath.resolve("objects").resolve("blobs").resolve(hash);

        if(Files.notExists(blobPath)){
            try{
            Files.write(blobPath, data);
            }
            catch (IOException e){
                throw new RuntimeException("Failed to save the blob" + hash, e);
            }
        }
        return hash;
    }

    public byte[] readBlob(String hash){
        Path blobPath = repoPath.resolve("objects").resolve("blobs").resolve(hash);
        if(Files.notExists(blobPath)){
            throw new RuntimeException("Blob" + hash + "not exists");
        }
        try{
            return Files.readAllBytes(blobPath);
        }
        catch (IOException e){
            throw new RuntimeException("Failed to read the blob " + hash, e);}
    }
}

