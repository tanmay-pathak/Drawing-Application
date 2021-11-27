package com.example.drawing_application;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The model for this drawing application. Stores the shapes and has method to check if a shape was clicked or not.
 * Uses publisher-subscriber model to notify its subscribers of any changes made to model by the controller.
 */
public class DrawingModel {
    /*
        Instance variables to store the shapes, the highest z-order for the latest shape and the list of model-subscribers.
     */
    private final ArrayList<XShape> shapes;
    private final ArrayList<ModelSubscriber> subs;
    XShape foundShape;
    int highestZOrder;

    /**
     * Default constructor for this class. Initializes the data-structure to store shapes and subscribers.
     */
    public DrawingModel() {
        this.subs = new ArrayList<>();
        this.shapes = new ArrayList<>();
        foundShape = new XSquare();
        highestZOrder = 0;
    }

    /**
     * Getter method for the model of shapes.
     *
     * @return : List of shapes in the model.
     */
    public ArrayList<XShape> getShapes() {
        return shapes;
    }


    /**
     * Method to add a subscriber which model will notify of any changes to its data structure.
     *
     * @param subscriber : a subscriber
     */
    public void addSubscriber(ModelSubscriber subscriber) {
        subs.add(subscriber);
    }

    /**
     * Helper method to notify all the subscribers of the changes to the model.
     */
    private void notifySubscribers() {
        subs.forEach(ModelSubscriber::modelChanged);
    }

    /**
     * Method to add a shape to the model.
     *
     * @param currentShape : shape to be added to the model.
     */
    public void addShape(XShape currentShape) {
        // Set the z-order of the newest shape
        currentShape.setZOrder(getHighestZOrder());
        foundShape = currentShape;
        shapes.add(currentShape);

        // sort the list in order of z-order
        shapes.sort(Comparator.comparingInt(XShape::getZOrder));
        notifySubscribers();
    }

    /**
     * Method to return the shape which had contains() called last.
     *
     * @return : selected shape where contains() returned true
     */
    public XShape foundShape() {
        return foundShape;
    }

    /**
     * Helper method to increment and return the highest z-order for a shape in the model.
     *
     * @return : highest z-order to be assigned to a shape
     */
    private int getHighestZOrder() {
        highestZOrder++;
        return highestZOrder - 1;
    }

    /**
     * Method to check if x,y coordinate of a mouse are inside a shape. Selects the top-most (highest z-order) shape
     * visible to the user and marks that as selected shape.
     *
     * @param x : x coordinate of the mouse click
     * @param y : y coordinate of the mouse click
     * @return : true if a shape found else false
     */
    public boolean contains(double x, double y) {
        // Sort list based on z-order (ascending)
        shapes.sort(Comparator.comparingInt(XShape::getZOrder));

        // Search in reverse to find the first shape with the highest z-order
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).contains(x, y)) {
                foundShape = shapes.get(i);
                // get the selected shape to the top
                foundShape.setZOrder(getHighestZOrder());
                return true;
            } else {
                if (resizeClicked(x, y)) {
                    // Special case: x, y not within shape but within the resize handle (oval, circle)
                    return true;
                }
            }
        }
        // no shape found - mark found shape as null
        foundShape = null;
        return false;
    }

    /**
     * Method to check if the resize handle was pressed for a shape.
     *
     * @param x : x coordinate to be searched
     * @param y : y coordinate to be searched
     * @return : true if resize handle was clicked for a shape else false
     */
    public boolean resizeClicked(double x, double y) {
        if (foundShape == null) {
            return false;
        }
        double resizeHandleSize = 0.005;

        // Set up the location of the resize-handle to check for hit
        double resizeLeft, resizeTop;
        // Use the given coordinate for line
        if (foundShape instanceof XLine) {
            resizeLeft = foundShape.getWidth() - 0.002;
            resizeTop = foundShape.getHeight() - 0.002;
        } else {
            // else, calculate the location
            resizeLeft = (foundShape.getLeft() + foundShape.getWidth()) - 0.003;
            resizeTop = (foundShape.getTop() + foundShape.getHeight()) - 0.003;
        }

        // check if the given x, y coordinate is within the resize handle
        double cx = resizeLeft + resizeHandleSize / 2;
        double cy = resizeTop + resizeHandleSize / 2;
        return Math.hypot(x - cx, y - cy) <= resizeHandleSize / 2;
    }

    /**
     * Method to create a new shape.
     *
     * @param shape : shape to be created 0 = rectangle, 1 = square, 2 = circle, 3 = oval and 4 = line
     * @param x     : initial x location for the newly created shape
     * @param y     :  initial y location for the newly created shape
     * @return : the newly created shape
     */
    public XShape createShape(int shape, double x, double y) {
        switch (shape) {
            case 0 -> foundShape = new XRectangle(x, y, 0, 0, getHighestZOrder());
            case 1 -> foundShape = new XSquare(x, y, 0, 0, getHighestZOrder());
            case 2 -> foundShape = new XCircle(x, y, 0, 0, getHighestZOrder());
            case 3 -> foundShape = new XOval(x, y, 0, 0, getHighestZOrder());
            case 4 -> foundShape = new XLine(x, y, x, y, getHighestZOrder());
        }
        notifySubscribers();
        return foundShape;
    }

    /**
     * Method to delete the last found shape.
     */
    public void deleteSelectedShape() {
        shapes.remove(foundShape);
        notifySubscribers();
    }
}
