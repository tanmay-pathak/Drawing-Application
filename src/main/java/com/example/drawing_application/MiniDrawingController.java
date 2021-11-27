package com.example.drawing_application;

import javafx.scene.input.MouseEvent;

/**
 * Controller for the mini-view. An extension of the main controller. Adds the functionality to move the view-finder
 * rectangle by left-click drag.
 */
public class MiniDrawingController extends DrawingController {
    /**
     * Default constructor for this class. Calls super's constructor.
     */
    public MiniDrawingController() {
        super();
    }

    /**
     * Over-ridden method from main controller. Adds the functionality to check if the mouse click is on the view-finder
     * rectangle.
     *
     * @param x          : X coordinate of the mouse click.
     * @param y          : Y coordinate of the mouse click.
     * @param mouseEvent : Mouse Event
     */
    @Override
    public void handlePressed(double x, double y, MouseEvent mouseEvent) {
        if (x >= iModel.getViewLeft() && x <= iModel.getViewLeft() + iModel.getViewFinderWidth() && y >= iModel.getViewTop() && y <= iModel.getViewTop() + iModel.viewFinderHeight) {
            currentState = State.PANNING;
        }
        super.handlePressed(x, y, mouseEvent);
    }

    /**
     * Over-ridden method from main controller. Adds the functionality to set new location for the view-finder rectangle.
     *
     * @param x          : X coordinate of the mouse click.
     * @param y          : Y coordinate of the mouse click.
     * @param mouseEvent : Mouse Event
     */
    @Override
    public void handleMove(double x, double y, MouseEvent mouseEvent) {
        if (currentState == State.PANNING) {
            iModel.setViewLeft(x - iModel.getViewFinderWidth() / 2);
            iModel.setViewTop(y - iModel.getViewFinderHeight() / 2);
        }
        super.handleMove(x, y, mouseEvent);
    }

    /**
     * Over-ridden method from main controller. Adds the functionality to switch states from Panning to Ready.
     *
     * @param x : X coordinate of the mouse click.
     * @param y : Y coordinate of the mouse click.
     * @param e : Mouse event.
     */
    @Override
    public void handleReleased(double x, double y, MouseEvent e) {
        if (currentState == State.PANNING) {
            currentState = State.READY;
        }
        super.handleReleased(x, y, e);
    }

    /**
     * Over-ridden helper method to calculate the x coordinate of the mouse click. Mini-controller does not need
     * to adjust for view-port's location.
     *
     * @param x : x coordinate to be adjusted
     * @return : new x coordinate of mouse click
     */
    @Override
    protected double getAdjustedX(double x) {
        return x;
    }

    /**
     * Over-ridden helper method to calculate the y coordinate of the mouse click. Mini-controller does not need
     * to adjust for view-port's location.
     *
     * @param y : y coordinate to be adjusted
     * @return : new y coordinate of mouse click
     */
    @Override
    protected double getAdjustedY(double y) {
        return y;
    }
}
