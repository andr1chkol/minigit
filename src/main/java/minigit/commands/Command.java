package minigit.commands;

import minigit.core.CommandRequest;

public interface Command {
    void execute(CommandRequest request);
}
