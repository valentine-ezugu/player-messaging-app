package com.valentine;

import java.util.function.Predicate;

public interface Publisher {

    String getPayload();

    Predicate<Listener> getExcludes();

}
