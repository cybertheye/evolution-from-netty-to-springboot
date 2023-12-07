package com.attackonarchitect.listener;

import java.util.List;

/**
 * @description:
 */

public interface Notifier {

    void addListeners(List<EventListener> eventListeners);

    void addListenersByPath(List<String> eventListeners);

    List<EventListener> getListeners();

    void notifyListeners(Event event);
}
