package com.valentine;

import java.util.function.Predicate;

// the player class is both the subscriber and publisher
public class Player implements Listener, EventBusAware {

    private final EventBus  bus;
    Integer sendIncrementCount = new Integer(0);
    Integer receiveIncrementCount = new Integer(0);
    private String name;

    public Player(String name, EventBus bus) {
        this.bus = bus;
        this.name = name;
    }

    @Override
    public void onMessage(String message) {
        System.out.println(String.format("thread: %d, name: %s , received message: %s ", Thread.currentThread().getId(), name, message));
        receiveIncrementCount++;
        sendMessage(message);
    }

    public void sendMessage(String message) {
        EventBus eventBus;
        if ((eventBus = this.bus) == null) {
            return;
        }
        int count;
        if (10 <= (count = ++sendIncrementCount) && (receiveIncrementCount <= 10)) {
            eventBus.onUnSubscribe(this);
        }

        eventBus.sendEventToSubscriber(new Publisher() {
            @Override
            public String getPayload() {
                String newMessage = message+"_" + count;
                System.out.println(String.format("thread: %d, name: %s , send message: %s ", Thread.currentThread().getId(), name, newMessage));
                return newMessage;
            }

            @Override
            public Predicate<Listener> getExcludes() {
                return predicate;
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getSendIncrementCount() {
        return sendIncrementCount;
    }

    public void setSendIncrementCount(Integer sendIncrementCount) {
        this.sendIncrementCount = sendIncrementCount;
    }

    public Integer getReceiveIncrementCount() {
        return receiveIncrementCount;
    }

    public void setReceiveIncrementCount(Integer receiveIncrementCount) {
        this.receiveIncrementCount = receiveIncrementCount;
    }

    @Override
    public void onSubscribe(EventBus bus) {
        bus = bus;
    }

    @Override
    public void onUnsubscribe(EventBus bus) {
        bus = null;
    }
    private final Predicate<Listener> predicate = (listener) -> listener.equals(Player.this);

}
