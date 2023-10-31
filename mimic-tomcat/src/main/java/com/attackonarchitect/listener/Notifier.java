package com.attackonarchitect.listener;

import java.util.List;

/**
 * @description:
 */

public interface Notifier {
    void notifyListeners(Class<?> listener ,Event event);
}
