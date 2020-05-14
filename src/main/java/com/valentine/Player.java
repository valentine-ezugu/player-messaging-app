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

    public void sendMessage(String message) {
        EventBus eventBus;
        if ((eventBus = bus.get()) == null) {
            return;
        }

        if (( sendIncrementCount.getAndIncrement() == 10) && (receiveIncrementCount.get() == 10)) {
            eventBus.onUnSubscribe(this);
        }
        int sendCount = sendIncrementCount.getAndIncrement();

        eventBus.sendEventToSubscriber(new Publisher() {
            @Override
            public String getPayload() {
                String newMessage = message + " " + sendCount;
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

    public void subscribeToEventBus(EventBus bus) {
        this.bus.set(bus);
    }
    public void unSubscribeToEventBus(EventBus bus) {
        this.bus.set(null);
    }

}
