package com.valentine;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * EventBus will be carrying events and handling subscription to events
 */
public class EventBus {

    private Dispatcher dispatcher;
    private String chartName;
    private ExecutorService executor;
    final CopyOnWriteArraySet<Subscriber> subscribers = new CopyOnWriteArraySet<Subscriber>();

    public EventBus(String chartName, ExecutorService executor) {
        this.executor = executor;
        this.chartName = chartName;
        this.dispatcher = new DispatcherService();
    }

    void onSubscribe(Listener listener) {
        subscribers.add(new Subscriber(this, listener));
    }

    void onUnSubscribe(Listener listener) {
        Set<Subscriber> subscriberstoRemove =
                getSubscribers().stream().filter(sub -> sub.getListener().equals(listener)).collect(Collectors.toSet());
        subscribers.removeAll(subscriberstoRemove);
    }

    void sendEventToSubscriber(Publisher publisher) {
        sendEventToSubscriber(publisher.getPayload());
    }

    /**
     * we must send iteratively because more than one subscriber may have subscribed to message
     * validSubscribers are subs that havent been used yet
     * @param payload
     */
    private void sendEventToSubscriber(String payload) {
        List<Subscriber> validSubscribers = subscribers.stream().filter(subs -> subs.getListener() != null).collect(toList());
        dispatcher.dispatch(payload, validSubscribers.iterator());
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public CopyOnWriteArraySet<Subscriber> getSubscribers() {
        return subscribers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventBus eventBus = (EventBus) o;
        return new EqualsBuilder()
                .append(chartName, eventBus.chartName)
                .append(executor, eventBus.executor)
                .append(subscribers, eventBus.subscribers)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(chartName)
                .append(executor)
                .append(subscribers)
                .toHashCode();
    }
}
