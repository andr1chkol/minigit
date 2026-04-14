package minigit.commands;

import minigit.core.CommandRequest;
import minigit.core.RepositoryPaths;
import minigit.domain.Commit;
import minigit.storage.CommitStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogCommand implements Command {
    public void execute(CommandRequest request){

        RepositoryPaths paths = RepositoryPaths.fromCurrentDirectory();
        paths.ensureInitialized();
        Path repoPath = paths.repoPath();

        try{
            String currentId;
            Path mainRef = paths.mainRefPath();

            List<String> mainLine = Files.readAllLines(mainRef);

            if(mainLine.isEmpty()){
                throw new RuntimeException("No commits yet");
            }
            else{
                currentId = mainLine.getFirst();
            }

            CommitStore commitStore = new CommitStore(repoPath);

            while(currentId != null){
                Commit commit = commitStore.readCommit(currentId);

                System.out.println("commit " + commit.getId());
                System.out.println("message: " + commit.getMessage());
                System.out.println("parent: " + commit.getParentId());
                System.out.println();

                currentId = commit.getParentId();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
