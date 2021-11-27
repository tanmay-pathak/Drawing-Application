package com.example.drawing_application;

import javafx.scene.paint.Color;

/**
 * Abstract class for a Shape. Supports move, resize, create, get and set colours and the z-order.
 */
public abstract class XShape {
    /*
        Variables to store required elements for a shape.
     */
    double left, top, width, height;
    int zOrder;
    Color color;

    /**
     * Default constructor for this class. Creates a new default black coloured shape.
     */
    public XShape() {
        this.left = 0;
        this.top = 0;
        this.width = 0;
        this.height = 0;
        this.color = Color.BLACK;
    }

    /**
     * Constructor for this class that supports adding the location, size and colour.
     *
     * @param left   : x coordinate of the top left corner of the shape
     * @param top    : y coordinate of the top left corner of the shape
     * @param width  : width of the shape
     * @param height : height of the shape
     * @param color  : colour of the shape
     */
    public XShape(double left, double top, double width, double height, Color color) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    /**
     * Constructor for this class that supports adding location, size and z-order.
     *
     * @param left   : x coordinate of the top left corner of the shape
     * @param top    : y coordinate of the top left corner of the shape
     * @param width  : width of the shape
     * @param height : height of the shape
     * @param zOrder : z-order of the shape
     */
    public XShape(double left, double top, double width, double height, int zOrder) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.zOrder = zOrder;
    }

    /**
     * Method to find out whether the given x & y coordinates are within the shape.
     *
     * @param x : x coordinate to be checked
     * @param y : y coordinate to be checked
     * @return : true if x & y within shape else false
     */
    public abstract boolean contains(double x, double y);

    /**
     * Method to resize the shape.
     *
     * @param x      : new x coordinate
     * @param y      : new y coordinate
     * @param width  : new width
     * @param height : new height
     */
    public void resize(double x, double y, double width, double height) {
        this.left = x;
        this.top = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Method to move shape.
     *
     * @param x : distance to move x coordinate by
     * @param y : distance to move y coordinate by
     */
    public void move(double x, double y) {
        this.left -= x;
        this.top -= y;
    }

    /**
     * Getter method for z-order.
     *
     * @return : z-order for the shape.
     */
    public int getZOrder() {
        return zOrder;
    }

    /**
     * Setter method for z-order.
     *
     * @param zOrder : new z-order for the shape.
     */
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    /**
     * Getter method for x coordinate of top-left corner.
     *
     * @return : x coordinate
     */
    public double getLeft() {
        return left;
    }

    /**
     * Setter method for x coordinate of top-left corner.
     *
     * @param left : new x coordinate
     */
    public void setLeft(double left) {
        this.left = left;
    }

    /**
     * Getter method for y coordinate of top-left corner.
     *
     * @return : y coordinate
     */
    public double getTop() {
        return top;
    }

    /**
     * Setter method for y coordinate of top-left corner.
     *
     * @param top : new y coordinate
     */
    public void setTop(double top) {
        this.top = top;
    }

    /**
     * Getter method for width of the shape.
     *
     * @return : width of shape
     */
    public double getWidth() {
        return width;
    }

    /**
     * Setter method for width of the shape.
     *
     * @param width : new width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Getter method for height of the shape.
     *
     * @return : height of shape
     */
    public double getHeight() {
        return height;
    }

    /**
     * Setter method for height of the shape.
     *
     * @param height : new height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Getter method for colour of the shape.
     *
     * @return : colour
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter method for colour of the shape.
     *
     * @param color : new colour
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
