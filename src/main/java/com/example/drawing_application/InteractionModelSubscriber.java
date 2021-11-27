package com.example.drawing_application;

/**
 * Interface for subscribers to implement methods to get notifications for iModel changes.
 */
public interface InteractionModelSubscriber {
    void iModelChanged();
}