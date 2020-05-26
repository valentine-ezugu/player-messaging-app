package com.valentine;

import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class DispatcherService implements Dispatcher {

    Queue<DispatcherService.Event> subscriberThread = new ArrayDeque<>();

    @Override
    public void dispatch(String payload, Iterator<Subscriber> subscribers) {
        Queue<Event> eventsThread = subscriberThread;
        eventsThread.offer(new Event(payload, subscribers));
            DispatcherService.Event eventQueue;
            while ((eventQueue = eventsThread.poll()) != null) {

                for (; eventQueue.subscribers.hasNext(); ) {
                    eventQueue.subscribers.next().sendEvent(eventQueue.payload);
                }
            }

    }

    private static class Event {
        private String payload;
        private Iterator<Subscriber> subscribers;

        public Event(String payload, Iterator<Subscriber> subscribers) {
            this.payload = payload;
            this.subscribers = subscribers;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public Iterator<Subscriber> getSubscribers() {
            return subscribers;
        }

        public void setSubscribers(Iterator<Subscriber> subscribers) {
            this.subscribers = subscribers;
        }
    }
}
