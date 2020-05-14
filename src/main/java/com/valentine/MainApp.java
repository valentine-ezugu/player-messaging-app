package com.valentine;

import java.util.concurrent.Executors;

public class MainApp {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus("game", Executors.newFixedThreadPool(1));
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
player1.subscribeToEventBus(eventBus);
player2.subscribeToEventBus(eventBus);
        eventBus.onSubscribe(player1);
        eventBus.onSubscribe(player2);

        player1.sendMessage("hey");
    }

}
