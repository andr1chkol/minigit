package minigit.commands;

import minigit.core.CommandRequest;
import minigit.domain.Commit;
import minigit.storage.CommitStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogCommand implements Command {
    public void execute(CommandRequest request){

        Path repoPath = Path.of(System.getProperty("user.dir"), ".minigit");
        if (Files.notExists(repoPath)) {
            throw new RuntimeException("Repository not initialized");
        }

        try{
            String currentId;
            Path mainRef = repoPath.resolve("refs").resolve("heads").resolve("main");

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
