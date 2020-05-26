package com.valentine;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * EventBus will be carrying events and handling subscription to events
 */
public class EventBus {

    private Dispatcher dispatcher;
    private String chartName;
    final Set<Subscriber> subscribers = new HashSet<>();
    public Boolean isEmpty = Boolean.TRUE;

    public EventBus(String chartName) {
        this.chartName = chartName;
        this.dispatcher = new DispatcherService();
    }

    public void onSubscribe(Listener listener) {
        subscribers.add(new Subscriber(this, listener));
        isEmpty = false;
        if (listener instanceof EventBusAware) {
            ((EventBusAware) listener).onSubscribe(this);
        }
    }

    public void onUnSubscribe(Listener listener) {
        Set<Subscriber> subscriberstoRemove =
                getSubscribers().stream().filter(sub -> sub.getListener().equals(listener)).collect(Collectors.toSet());
        subscribers.removeAll(subscriberstoRemove);
        isEmpty = subscriberstoRemove.isEmpty();
        if (listener instanceof EventBusAware) {
            ((EventBusAware) listener).onUnsubscribe(this);
        }
    }

    public void sendEventToSubscriber(Publisher publisher) {
        sendEventToSubscriber(publisher.getPayload(), publisher.getExcludes());
    }

    /**
     * we must send iteratively because more than one subscriber may have subscribed to message.
     * validSubscribers are subs that havent been used yet
     * filters out the sender (player ) and dispatch to other player (reciever-)
     * @param payload message
     *
     */
    private void sendEventToSubscriber(String payload, Predicate<Listener> excludes) {
        Predicate<Listener> predicate = null == excludes ? (s) -> true : excludes.negate();

        Set<Subscriber> subscribers = this.subscribers.stream()
                .filter(subscriber -> predicate.test(subscriber.getListener())) //get subscribers that have been exlcuded
                .collect(Collectors.toSet());
        dispatcher.dispatch(payload, subscribers.iterator());
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Set<Subscriber> getSubscribers() {
        return subscribers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventBus eventBus = (EventBus) o;
        return new EqualsBuilder()
                .append(dispatcher, eventBus.dispatcher)
                .append(chartName, eventBus.chartName)
                .append(subscribers, eventBus.subscribers)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(dispatcher)
                .append(chartName)
                .append(subscribers)
                .toHashCode();
    }


    public boolean isEmpty() {
        return isEmpty;
    }
}
