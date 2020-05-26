package com.valentine;


import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import static java.lang.System.exit;
import static java.lang.System.out;
import static java.util.stream.Collectors.toMap;

public class MainApp {


    private static final String USAGE_STRING = "java -jar players-app-{version}.jar [-help]" +
            " <chatName> [playerNic1],[playerNic2],[...] <initiatorNic> <message>";

    private static final Options options;

    static {
        options = new Options();

        options.addOption(Option.builder("help")
                .required(false)
                .hasArg(false)
                .desc("print this message")
                .build());
    }

    private static EventBus bus;
    private static Map<String, Player> chatMembers = new HashMap<>();
    private static String initiatorNic;
    private static String startMessage;

    public static void main(String[] args) {
        try {

            initChat(args);

            run();

        } catch (Exception e) {
            printUsage(e.getMessage());
        }
    }

    /**
     * Sending initial message
     */
    private static void run() {
        chatMembers.get(initiatorNic).sendMessage(startMessage);
        while (true) {
            if (bus.isEmpty()) {
                System.out.println("Shutting down ...");
                exit(0);
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static void initChat(String... args) {
        out.println("initializing..");
        CommandLine commandLine;
        try {
            commandLine = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if (commandLine.hasOption("help")) {
            printUsage();
            exit(0);
        }

        if (commandLine.getArgs().length < 3) {
            throw new IllegalArgumentException("not enough input parameters");
        }

        setUpEventBus(commandLine.getArgs()[0]);
        setUpPlayers(commandLine.getArgs()[1].split(","));
        setUpInitiator(commandLine.getArgs()[2]);
        startMessage = commandLine.getArgs()[3];

        out.println(String.format("chat {%s} started..", bus.getChartName()));
    }

    private static void setUpEventBus(String chatName) {
        bus = new EventBus(chatName);
    }

    private static void setUpPlayers(String... players) {
        chatMembers = Arrays.stream(players)
                .map((String name) -> new Player(name, bus))
                  .collect(toMap(player -> player.getName(), Function.identity()));

        chatMembers.values().forEach(player -> {
            bus.onSubscribe(player);
            chatMembers.put(player.getName(), player);
            out.println(String.format("Player {%s} has been joined to chat {%s}.", player.getName(), bus.getChartName()));
        });
    }

    private static void setUpInitiator(String initiator) {
        if (!chatMembers.containsKey(initiatorNic = initiator)) {
            throw new IllegalArgumentException(String.format("initiatorNic {%s} not found in the chat.", initiatorNic));
        }
    }

    private static void printUsage(String message) {
        out.println();
        out.println(message);
        printUsage();
    }

    private static void printUsage() {
        out.println();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(120, USAGE_STRING, "", options, "");
        out.println();
    }


}
