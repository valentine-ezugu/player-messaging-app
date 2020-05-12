package com.valentine;

import java.util.Iterator;

public interface Dispatcher {

   void dispatch(String payload, Iterator<Subscriber> subscribers);
}
