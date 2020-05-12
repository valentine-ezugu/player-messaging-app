package com.valentine;

import java.util.concurrent.Executor;

/**
 * Subscriber here is a using the listener to alert the player(which is the actual subscriber in this case ) about new message
 */
public class Subscriber {

    private EventBus eventBus;
    // representing the player who will be listening
    private Listener listener;
    private Executor executor;

    Subscriber(EventBus eventBus, Listener listener) {
        this.eventBus = eventBus;
        this.listener = listener;
        this.executor = eventBus.getExecutor();
    }

    /**
     *
     * @param listener alerts player about new message
     */
    final void sendEvent(final String event) {
        synchronized (this) {
            executor.execute(() -> {
                listener.onMessage(event);
            });
        }
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


}
