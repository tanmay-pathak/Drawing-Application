package com.example.drawing_application;

/**
 * Interface for subscribers to implement methods to get notifications for model changes.
 */
public interface ModelSubscriber {
    void modelChanged();
}