package com.valentine;

import java.util.Iterator;

/**
 * Represents sending events to subscribers
 */
public interface Dispatcher {

   void dispatch(String payload, Iterator<Subscriber> subscribers);
}
