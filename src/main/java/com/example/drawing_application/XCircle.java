package com.example.drawing_application;

/**
 * Shape to represent a circle. An extension of XShape.
 */
public class XCircle extends XShape {
    /**
     * Default constructor for this class. Calls super's constructor.
     */
    public XCircle() {
        super();
    }

    /**
     * Constructor to create a new circle with the location, size and a z-order.
     *
     * @param left   : x coordinate of top left corner
     * @param top    : y coordinate of top left corner
     * @param width  : width of the shape
     * @param height : height of the shape
     * @param z      : z-order of the shape
     */
    public XCircle(double left, double top, double width, double height, int z) {
        super(left, top, width, height, z);
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
        // calculate the center point of the circle
        double cx = left + width / 2;
        double cy = top + height / 2;

        return Math.hypot(x - cx, y - cy) <= width / 2;
    }
}
