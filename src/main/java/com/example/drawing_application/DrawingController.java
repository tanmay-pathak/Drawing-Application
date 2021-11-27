package com.example.drawing_application;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Main controller for the MVC architecture. Over-loads mouse press, move and released to different actions.
 * Supports creation, selection, move, resizing and deletion of shapes as well as, panning of the view port.
 */
public class DrawingController {
    /*
        Instance variables to store the current state of the state-machine, model, iModel and mouse-click location to
        support moving and resizing.
     */
    protected State currentState;
    DrawingModel model;
    InteractionModel iModel;
    double prevX, prevY;
    boolean existingShape;

    /*
        States for the state-machine. Helps over-load mouse interactions.
     */
    protected enum State {
        READY, PREPARE_CREATE, CREATING, MOVING, SELECTION, RESIZING, PANNING
    }

    /**
     * Default constructor for this class. Sets up the state-machine in the ready state.
     */
    public DrawingController() {
        currentState = State.READY;
        existingShape = false;
    }

    /**
     * Method to facilitate the selection of the current shape from shape toolbar to iModel.
     *
     * @param newShape : Number representing the shape selected by the user.
     */
    public void setNewShape(int newShape) {
        iModel.setCurrentShapeType(newShape);
    }

    /**
     * Method to set up a model - as part of the MVC architecture.
     *
     * @param model : The Model for the controller.
     */
    public void setModel(DrawingModel model) {
        this.model = model;
    }

    /**
     * Method to set up an interactive model - as part of the MVC architecture.
     *
     * @param iModel : The iModel for the controller.
     */
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    /**
     * Method to facilitate the selection of the current colour by the user from colour toolbar to iModel.
     *
     * @param col : the colour to be set
     */
    public void setCurrentColour(Color col) {
        iModel.setCurrentColour(col);
    }

    /**
     * Method to facilitate changing the width of the view finder in iModel by the Drawing view.
     *
     * @param doubleValue : New width of the view finder.
     */
    public void setViewFinderWidth(double doubleValue) {
        iModel.setViewFinderWidth(doubleValue);
    }

    /**
     * Method to facilitate changing the height of the view finder in iModel by the Drawing view.
     *
     * @param doubleValue : New width of the view finder.
     */
    public void setViewFinderHeight(double doubleValue) {
        iModel.setViewFinderHeight(doubleValue);
    }

    /**
     * Method to handle key pressed on the keyboard by the user. Deletes the selected shape when DELETE key is pressed.
     *
     * @param keyEvent : Keyboard Key Event
     */
    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            model.deleteSelectedShape();
            iModel.setSelectedShape(model.createShape(iModel.getCurrentShapeType(), 0, 0));
        }
    }

    /**
     * Method to handle a mouse click by the user. Primary click supports creation, selection, move and resize of a shape.
     * Secondary click supports panning of the view port.
     *
     * @param x          : X coordinate of the mouse click.
     * @param y          : Y coordinate of the mouse click.
     * @param mouseEvent : Mouse Event
     */
    public void handlePressed(double x, double y, MouseEvent mouseEvent) {
        // Switch to handle primary or secondary mouse clicks
        switch (mouseEvent.getButton()) {
            case PRIMARY -> {
                // Adjust the x and y coordinate of the mouse click based on where mouse is, in respect to the world/document
                x = getAdjustedX(x);
                y = getAdjustedY(y);

                // Switch based on the current state of the state-machine
                switch (currentState) {
                    case READY -> {
                        if (model.contains(x, y)) {
                            // Action: Left Click | Context: OnShape | Side Effect: Select Shape, move shape to the top
                            // and show bounding box
                            // Switch state from Ready to Moving
                            currentState = State.MOVING;
                            iModel.setSelectedShape(model.foundShape());
                            prevX = x;
                            prevY = y;
                        } else {
                            // Action: Left Click | Context: OnBackground | Side Effect: Create a temporary shape
                            // Switch state from Ready to Prepare to Create
                            currentState = State.PREPARE_CREATE;
                            iModel.setSelectedShape(model.createShape(iModel.getCurrentShapeType(), x, y));
                        }
                    }
                    case SELECTION -> {
                        boolean shapeHit = model.contains(x, y);
                        boolean resizeHit = model.resizeClicked(x, y);
                        if (shapeHit && !resizeHit) {
                            // Action: Left Click | Context: OnShape | Side Effect: None OR if different shape selected, select that shape
                            // Switch state from Selection to Moving
                            iModel.setSelectedShape(model.foundShape());
                            currentState = State.MOVING;
                            prevX = x;
                            prevY = y;
                        } else if (shapeHit) {
                            // Action: Left Click | Context: OnResize Handle | Side Effect: None OR if different shape selected, select that shape
                            // Switch state from Selection to Resizing
                            currentState = State.RESIZING;
                            iModel.setSelectedShape(model.foundShape());
                            prevX = iModel.getSelectedShape().getLeft();
                            prevY = iModel.getSelectedShape().getTop();
                        } else {
                            // Action: Left Click | Context: On background | Side Effect: Unselect shape, don't show bounding box and create a new temp shape
                            currentState = State.PREPARE_CREATE;
                            iModel.setSelectedShape(model.createShape(iModel.getCurrentShapeType(), x, y));
                        }
                    }
                }
            }
            case SECONDARY -> {
                // Switch state to Panning
                // Action: Right Click | Context: None | Side Effect: None
                currentState = State.PANNING;

                // Save x, y coordinates to help find how much to pan the view-port by
                prevX = x;
                prevY = y;
            }
        }
    }

    /**
     * Method to handle mouse drag by the user. Mouse drag can resize and move the shape or pan the viewport.
     *
     * @param x          : X coordinate of the mouse click.
     * @param y          : Y coordinate of the mouse click.
     * @param mouseEvent : Mouse Event
     */
    public void handleMove(double x, double y, MouseEvent mouseEvent) {
        // Switch to handle primary or secondary mouse clicks
        switch (mouseEvent.getButton()) {
            case PRIMARY -> {
                // Adjust the x and y coordinate of the mouse click based on where mouse is, in respect to the world/document
                x = getAdjustedX(x);
                y = getAdjustedY(y);
                switch (currentState) {
                    case PREPARE_CREATE -> {
                        // Action: Mouse Drag of Left Click | Context: None | Side effect: create a temporary shape
                        // State switch from Prepare to Create -> Dragging
                        prevX = x;
                        prevY = y;
                        currentState = State.CREATING;
                        iModel.setSelectedShape(model.createShape(iModel.getCurrentShapeType(), x, y));
                    }
                    case CREATING -> {
                        // Action: Mouse Drag of Left Click | Context: None | Side effect: Resize the temporary shape in iModel
                        // State remains the same at DRAGGING
                        XShape curShape = iModel.getSelectedShape();
                        resize(prevX, prevY, x, y, curShape);
                    }
                    case MOVING -> {
                        // Action: Mouse Drag of Left Click | Context: None | Side Effect: Move shape to new location
                        // Pass distance of mouse move for moving line and current x, y of mouse for other shapes
                        iModel.moveShape(prevX - x, prevY - y);
                        prevX = x;
                        prevY = y;

                    }
                    case RESIZING -> {
                        // Action: Mouse Drag of Left Click | Context: None | Side Effect: Resize the selected shape
                        XShape shape = iModel.getSelectedShape();
                        model.deleteSelectedShape();
                        existingShape = true;
                        resize(prevX, prevY, x, y, shape);
                    }
                }
            }
            case SECONDARY -> {
                // Add the distance moved by mouse to the viewport starting points
                iModel.setViewLeft(iModel.getViewLeft() + (prevX - x));
                iModel.setViewTop(iModel.getViewTop() + (prevY - y));
                prevX = x;
                prevY = y;
            }
        }
    }

    /**
     * Method to handle mouse released by the user. Helps to finish up resizing, creating and moving of shapes.
     *
     * @param x          : X coordinate of the mouse click.
     * @param y          : Y coordinate of the mouse click.
     * @param mouseEvent : Mouse Event
     */
    public void handleReleased(double x, double y, MouseEvent mouseEvent) {
        // Switch based on which mouse button is released
        switch (mouseEvent.getButton()) {
            case PRIMARY -> {
                // Adjust the x and y coordinate of the mouse click based on where mouse is, in respect to the world/document
                x = getAdjustedX(x);
                y = getAdjustedY(y);
                switch (currentState) {
                    case PREPARE_CREATE -> {
                        // Action: Left Click released | Context: None | Side Effect: Cancel preparing to create and create new temp shape
                        // State switch from Prepare to Create -> Ready
                        currentState = State.READY;
                        iModel.setSelectedShape(model.createShape(iModel.getCurrentShapeType(), x, y));
                    }
                    case CREATING -> {
                        // Action: Left Click released | Context: None | Side effect: Add the temporary shape to model
                        // State switch from Creating -> Ready
                        model.addShape(iModel.getSelectedShape());
                        currentState = State.READY;
                    }
                    case MOVING -> {
                        // Action: Left Click released | Context: None | Side effect: None
                        // State switch from Moving -> Selection
                        currentState = State.SELECTION;
                    }
                    case RESIZING -> {
                        // Action: Left Click released | Context: None | Side effect: Add the resized shape to the model
                        // State switch from Resizing -> Selection
                        existingShape = false;
                        model.addShape(iModel.getSelectedShape());
                        currentState = State.SELECTION;
                    }
                }
            }
            case SECONDARY -> {
                // Action: Right Click released | Context: None | Side effect: None
                // State switch from Panning -> Ready
                currentState = State.READY;
            }
        }
    }

    /**
     * Helper method to adjust the value of x coordinate based on the view-port.
     *
     * @param x : x coordinate to be adjusted
     * @return : adjusted x coordinate
     */
    protected double getAdjustedX(double x) {
        x += iModel.getViewLeft();
        return x;
    }

    /**
     * Helper method to adjust the value of y coordinate based on the view-port.
     *
     * @param y : y coordinate to be adjusted
     * @return : adjusted y coordinate
     */
    protected double getAdjustedY(double y) {
        y += iModel.getViewTop();
        return y;
    }

    /**
     * Helper method to help controller resize the newly created or existing shape based on which shape is selected.
     *
     * @param prevX    : initial x coordinate
     * @param prevY    : initial y coordinate
     * @param x        : current x coordinate
     * @param y        : current y coordinate
     * @param curShape : shape to resize
     */
    private void resize(double prevX, double prevY, double x, double y, XShape curShape) {
        double x1, y1, dX, dY, w, h, size;
        Color color;

        // Set colour based on if the shape already exists, or it is a new one
        if (existingShape) {
            color = curShape.getColor();
        } else {
            color = null;
        }

        x1 = Math.min(prevX, x);
        y1 = Math.min(prevY, y);
        w = Math.abs(prevX - x);
        h = Math.abs(prevY - y);
        size = Math.min(w, h);
        dX = x - prevX;
        dY = y - prevY;

        // handle resizing of line - line does not take width and height. it takes old x,y and new x,y
        if (curShape instanceof XLine) {
            iModel.resizeShape(prevX, prevY, x, y, color);
        }
        // handle resizing of square and circle - both have constraints on size and the starting points need to change
        else if (curShape instanceof XSquare || curShape instanceof XCircle) {
            // switch control/starting points based on which quadrant the shape should be from the initial click
            // for drawing on quadrant 3
            if (dX < 0 && dY > 0) {
                x1 = prevX - size;
            }
            // for drawing on quadrant 2
            else if (dX < 0 && dY < 0) {
                x1 = prevX - size;
                y1 = prevY - size;
            }
            // for drawing on quadrant 1
            else if (dX > 0 && dY < 0) {
                x1 = prevX;
                y1 = prevY - size;
            }
            iModel.resizeShape(x1, y1, size, size, color);
        }
        // handle resizing of rectangle and oval
        else {
            iModel.resizeShape(x1, y1, w, h, color);
        }
    }
}
