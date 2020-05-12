package com.valentine;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Player implements Listener {

    private final AtomicReference<EventBus> bus = new AtomicReference<>();

    AtomicInteger sendIncrementCount = new AtomicInteger();
    AtomicInteger receiveIncrementCount = new AtomicInteger();
    private String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public void onMessage(String message) {

        System.out.println(String.format("thread: %d, name: %s , received message: %s ", Thread.currentThread().getId(), name, message));

        receiveIncrementCount.getAndIncrement();

        sendMessage(message);
    }

    private void sendMessage(String message) {
        EventBus eventBus;
        if ((eventBus = bus.get()) == null) {
            return;
        }

        int sendCount = sendIncrementCount.getAndIncrement();
        if (sendCount == 10 && (receiveIncrementCount.get() == 10)) {
            eventBus.onUnSubscribe(this);
        }

        eventBus.sendEventToSubscriber(new Publisher() {
            @Override
            public String getPayload() {
                String newMessage = message + "" + sendCount;
                System.out.println(String.format("thread: %d, name: %s , send message: %s ", Thread.currentThread().getId(), name, newMessage));
                return newMessage;
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
