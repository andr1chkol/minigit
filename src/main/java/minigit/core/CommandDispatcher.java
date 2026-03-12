package minigit.core;

import minigit.commands.Commands;
import minigit.commands.InitCommand;

import java.util.HashMap;

public class CommandDispatcher {
    private final HashMap<String, Commands> commands =  new HashMap<>();

    public CommandDispatcher(){
        commands.put("init", new InitCommand());
    }

    public void dispatch(CommandRequest request){
        Commands command = commands.get(request.getCommand());
        if(command == null){
            System.out.println("No command found for " + request.getCommand());
            return;
        }
        command.execute(request);
    }
}
