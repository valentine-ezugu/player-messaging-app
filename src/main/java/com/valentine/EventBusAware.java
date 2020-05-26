package com.valentine;

public interface EventBusAware {

    /**
     * Set the EventBus that this object runs in.
     * @param bus event bus
     */
    void onSubscribe(EventBus bus);

    /**
     * Remove the EventBus from this object.
     * @param bus  Removed bus
     */
    void onUnsubscribe(EventBus bus);
}
