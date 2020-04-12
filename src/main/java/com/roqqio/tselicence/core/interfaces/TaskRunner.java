package com.roqqio.tselicence.core.interfaces;

import java.util.concurrent.Executors;

public interface TaskRunner {
    default void run(Runnable runnable) {
        Executors.newCachedThreadPool().execute(runnable);
    }
}
