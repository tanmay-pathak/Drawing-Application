package com.example.drawing_application;

/**
 * Shape to represent a line. An extension of XShape.
 */
public class XLine extends XShape {
    /**
     * Default constructor for this class. Calls super's constructor.
     */
    public XLine() {
        super();
    }

    /**
     * Constructor to create a new line with the location, size and a z-order.
     *
     * @param x1     : x coordinate of the start of the line
     * @param y1     : y coordinate of the start of the line
     * @param x2     : x coordinate of the end of the line
     * @param y2     : y coordinate of the end of the line
     * @param zOrder : z-order
     */
    public XLine(double x1, double y1, double x2, double y2, int zOrder) {
        super(x1, y1, x2, y2, zOrder);
    }

    /**
     * Method to check whether the given x & y coordinates are within the shape.
     *
     * @param mx : x coordinate to be checked
     * @param my : y coordinate to be checked
     * @return : true if yes else false
     */
    @Override
    public boolean contains(double mx, double my) {
        double x1, y1, x2, y2;
        double ratioA, ratioB, ratioC;

        // line coordinates
        x1 = left;
        y1 = top;
        x2 = width;
        y2 = height;
        double lengthOfLine = dist(x1, y1, x2, y2);

        // tolerance to allow for a click to represent a click on the line
        double tolerance = 0.008;

        // ratios to check how far is the given point from the line
        ratioA = (y1 - y2) / lengthOfLine;
        ratioB = (x2 - x1) / lengthOfLine;
        ratioC = -1 * ((y1 - y2) * x1 + (x2 - x1) * y1) / lengthOfLine;

        double distanceFromLine = ratioA * mx + ratioB * my + ratioC;

        // check if the given point is close to the line
        boolean closeToLine = (Math.abs(distanceFromLine) < tolerance);

        // check if the given points are within the bounds of the line
        boolean withinBoundsX = ((mx >= x1) && (mx <= x2));
        boolean withinBoundsY = ((my >= y1) && (my <= y2));

        // Edge case: Where the lines are almost vertical or horizontal
        if ((x2 - x1) <= tolerance) {
            withinBoundsX = true;
        } else if ((y2 - y1) <= tolerance) {
            withinBoundsY = true;
        }

        return closeToLine && withinBoundsX && withinBoundsY;
    }

    /**
     * Helper method to calculate the distance between two points.
     *
     * @param x1 : x coordinate of first point
     * @param y1 : y coordinate of first point
     * @param x2 : x coordinate of second point
     * @param y2 : y coordinate of second point
     * @return : distance between points
     */
    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Method to move the line to a new location. Line stores x and y coordinates of
     * start and end points. Both to be adjusted.
     *
     * @param dX : distance to move x coordinate by
     * @param dY : distance to move y coordinate by
     */
    @Override
    public void move(double dX, double dY) {
        this.left -= dX;
        this.top -= dY;
        this.width -= dX;
        this.height -= dY;
    }
}
