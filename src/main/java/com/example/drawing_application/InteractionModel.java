package com.example.drawing_application;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

/**
 * iModel for the MVC architecture. Stores the selected colour, shape and supports method to get and set them. Uses
 * publisher-subscriber model to notify subscribers of the changes done.
 */
public class InteractionModel {
    private final ArrayList<InteractionModelSubscriber> subs;
    /*
        Instance variables to store the selected shape, colour, view-port dimensions and a list of subscribers.
     */
    XShape selectedShape;
    Color currentColour;
    double viewLeft, viewTop, viewFinderWidth, viewFinderHeight;
    int currentShapeType;
    double resizeHandleSize;


    /**
     * Default constructor for this class. Sets up the list of subscribers and the default shape and colours.
     */
    public InteractionModel() {
        this.subs = new ArrayList<>();

        // default shape selected is square and colour aqua
        currentColour = Color.AQUA;
        selectedShape = new XSquare();
        currentShapeType = 1;
        resizeHandleSize = 0.005;
    }

    /**
     * Getter method to get x coordinate to the start of the view-port.
     *
     * @return : x coordinate for the start of the view-port
     */
    public double getViewLeft() {
        return viewLeft;
    }

    /**
     * Setter method to set x coordinate to the start of the view-port.
     *
     * @param viewLeft : value to be used
     */
    public void setViewLeft(double viewLeft) {
        // bounds of the view-port
        if (viewLeft < 0.000) {
            this.viewLeft = 0.000;
        } else if (viewLeft > (0.999 - viewFinderWidth)) {
            this.viewLeft = (0.999 - viewFinderWidth);
        } else {
            this.viewLeft = viewLeft;
        }
        notifySubscriber();
    }

    /**
     * Getter method to get y coordinate to the start of the view-port.
     *
     * @return : y coordinate for the start of the view-port
     */
    public double getViewTop() {
        return viewTop;
    }

    /**
     * Setter method to set y coordinate to the start of the view-port.
     *
     * @param viewTop : value to be used
     */
    public void setViewTop(double viewTop) {
        // bounds of the view-port
        if (viewTop < 0.000) {
            this.viewTop = 0.000;
        } else if (viewTop > (0.999 - viewFinderHeight)) {
            this.viewTop = 0.999 - viewFinderHeight;
        } else {
            this.viewTop = viewTop;
        }
        notifySubscriber();
    }

    /**
     * Getter method for the resize-handle size.
     *
     * @return : Size of the resize-handle.
     */
    public double getResizeHandleSize() {
        return resizeHandleSize;
    }

    /**
     * Method to add a subscriber to notify of iModel changes.
     *
     * @param subscriber : subscriber to be added
     */
    public void addISubscriber(InteractionModelSubscriber subscriber) {
        subs.add(subscriber);
    }

    /**
     * Method to notify all the subscribers.
     */
    private void notifySubscriber() {
        subs.forEach(InteractionModelSubscriber::iModelChanged);
    }

    /**
     * Method to get the current selected colour.
     *
     * @return : current colour
     */
    public Paint getCurrentColour() {
        return currentColour;
    }

    /**
     * Method to set the current colour.
     *
     * @param col : colour to be used
     */
    public void setCurrentColour(Color col) {
        currentColour = col;
        notifySubscriber();
    }

    /**
     * Method to resize the selected shape.
     *
     * @param x     : x coordinate
     * @param y     : y coordinate
     * @param w     : width
     * @param h     : height
     * @param color : colour to be stored
     */
    public void resizeShape(double x, double y, double w, double h, Color color) {
        // for a new shape add the current selected colour
        if (color == null) {
            color = currentColour;
        }
        selectedShape.setColor(color);
        selectedShape.resize(x, y, w, h);
        notifySubscriber();
    }

    /**
     * Method to get the current selected shape.
     *
     * @return : shape currently selected
     */
    public XShape getSelectedShape() {
        return selectedShape;
    }

    /**
     * Method to set the selected shape of iModel.
     *
     * @param selectedShape : new shape to be selected
     */
    public void setSelectedShape(XShape selectedShape) {
        this.selectedShape = selectedShape;
        notifySubscriber();
    }

    /**
     * Method to move the selected shape.
     *
     * @param x : new x coordinate
     * @param y : new y coordinate
     */
    public void moveShape(double x, double y) {
        selectedShape.move(x, y);
        notifySubscriber();
    }

    /**
     * Method to get the view-port's width
     *
     * @return : width of the view-port
     */
    public double getViewFinderWidth() {
        return viewFinderWidth;
    }

    /**
     * Method to set the view-port's width
     *
     * @param viewFinderWidth : new width of the view-port
     */
    public void setViewFinderWidth(double viewFinderWidth) {
        this.viewFinderWidth = viewFinderWidth;
        notifySubscriber();
    }

    /**
     * Method to get the view-port's height
     *
     * @return : height of the view-port
     */
    public double getViewFinderHeight() {
        return viewFinderHeight;
    }

    /**
     * Method to set the view-port's height
     *
     * @param viewFinderHeight : new height of the view-port
     */
    public void setViewFinderHeight(double viewFinderHeight) {
        this.viewFinderHeight = viewFinderHeight;
        notifySubscriber();
    }

    /**
     * Method to get the current selected shape.
     *
     * @return : shape selected in iModel
     */
    public int getCurrentShapeType() {
        return currentShapeType;
    }

    /**
     * Method to set a shape as the current selected shape.
     *
     * @param newShape : new shape
     */
    public void setCurrentShapeType(int newShape) {
        this.currentShapeType = newShape;
    }
}
