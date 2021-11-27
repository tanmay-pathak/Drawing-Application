package com.example.drawing_application;

/**
 * Shape to represent a rectangle. An extension of XShape.
 */
public class XRectangle extends XShape {
    /**
     * Default constructor for this class. Calls super's constructor.
     */
    public XRectangle() {
        super();
    }

    /**
     * Constructor to create a new rectangle with the location, size and a z-order.
     *
     * @param left   : x coordinate of the top-left corner of the shape
     * @param top    : y coordinate of the top-left corner of the shape
     * @param width  : width of shape
     * @param height : height of shape
     * @param zOrder : z-order of shape
     */
    public XRectangle(double left, double top, double width, double height, int zOrder) {
        super(left, top, width, height, zOrder);
    }

    /**
     * Method to check whether the given x & y coordinates are within the shape.
     *
     * @param x : x coordinate to be checked
     * @param y : y coordinate to be checked
     * @return : true if yes else false
     */
    @Override
    public boolean contains(double x, double y) {
        return x >= left && x <= left + width && y >= top && y <= top + height;
    }
}
