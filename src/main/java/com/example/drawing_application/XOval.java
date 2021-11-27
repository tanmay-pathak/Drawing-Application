package com.example.drawing_application;

/**
 * Shape to represent an oval. An extension of XShape.
 */
public class XOval extends XShape {
    /**
     * Default constructor for this class. Calls super's constructor.
     */
    public XOval() {
        super();
    }

    /**
     * Constructor to create a new oval with the location, size and a z-order.
     *
     * @param left   : x coordinate of the top-left corner of the shape
     * @param top    : y coordinate of the top-left corner of the shape
     * @param width  : width of shape
     * @param height : height of shape
     * @param zOrder : z-order of shape
     */
    public XOval(double left, double top, double width, double height, int zOrder) {
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
        /*
            Oval can be thought of as a circle which is scaled to one or both directions. To check if a point is within
            the shape we can scale the oval back to a circle and use the check for a circle which is simpler.
         */
        double scaledSize = width;
        double scaledX = x;
        double scaledY = y;
        double scaleFactor;
        boolean scaledWidth = false;

        // check which direction the oval is scaled and un-scale
        if (width > height) {
            scaleFactor = width / height;
            scaledSize = width / scaleFactor;
            scaledX = x / scaleFactor;
            scaledWidth = true;
        } else if (height > width) {
            scaleFactor = height / width;
            scaledY = y / scaleFactor;
        } else {
            // if not scaled simply check like a circle
            double centerX = left + width / 2;
            double centerY = top + height / 2;
            return Math.hypot(x - centerX, y - centerY) <= width / 2;
        }

        // scale the center as well
        double centerX = left + width / 2;
        double centerY = top + height / 2;
        if (scaledWidth) {
            centerX = centerX / scaleFactor;
        } else {
            centerY = centerY / scaleFactor;
        }

        // check the un-scaled version of the oval
        return Math.hypot(scaledX - centerX, scaledY - centerY) <= scaledSize / 2;
    }
}
