package minigit.core;

import minigit.commands.Command;
import minigit.commands.InitCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
    private final Map<String, Command> commands =  new HashMap<>();

    public CommandDispatcher(){
        commands.put("init", new InitCommand());
    }

    public void dispatch(CommandRequest request){
        Command command = commands.get(request.getCommand());
        if(command == null){
            System.out.println("Unknown" + request.getCommand());
            return;
        }
        command.execute(request);
    }
}
