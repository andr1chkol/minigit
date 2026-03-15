package minigit.cli;

import minigit.core.CommandRequest;
import minigit.core.CommandDispatcher;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No command provided");
            return;
        }
        String command = args[0];
        List<String> arguments = Arrays.asList(args).subList(1, args.length);

        CommandRequest request = new CommandRequest(command, arguments);
        CommandDispatcher dispatcher = new CommandDispatcher();

        dispatcher.dispatch(request);
    }
}
