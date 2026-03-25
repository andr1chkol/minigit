package minigit.core;

import java.util.List;

public class CommandRequest {
    private final String command;
    private final List<String> args;

    public CommandRequest(String command, List<String> arguments) {
        this.command = command;
        this.args = arguments;
    }
    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }
}
