package com.example.drawing_application;

import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * View class that creates 5 buttons to let the user select which shape to create on to the canvas. The shapes in order
 * are: Rectangle, Square, Circle, Oval and Line. The selected shape has a drop-shadow and a fill of the selected colour
 * in the iModel.
 */
public class ShapeToolbar extends VBox implements InteractionModelSubscriber {
    /*
        Instance variables to store buttons, graphics and the list to contain them.
     */
    InteractionModel iModel;
    ToggleButton buttonRect, buttonSquare, buttonCircle, buttonOval, buttonLine;
    Rectangle rect;
    Rectangle square;
    Circle circle;
    Ellipse oval;
    Line line;
    ToggleButton selectedButton;
    Shape selectedShape;
    ToggleButton[] buttons;
    Shape[] shapes;

    /**
     * Default constructor for this class. Sets up the buttons and adds graphic shapes to them.
     */
    public ShapeToolbar() {
        super();
        setupGraphics();
        setupButtons();
    }

    /**
     * Helper method to set up the graphic shapes.
     */
    private void setupGraphics() {
        rect = new Rectangle(0, 0, 30, 20);
        square = new Rectangle(0, 0, 20, 20);
        circle = new Circle(0, 0, 10);
        oval = new Ellipse(0, 0, 15, 10);
        line = new Line(0, 0, 10, 10);
        shapes = new Shape[]{rect, square, circle, oval, line};
    }

    /**
     * Helper method to set up all the buttons.
     */
    private void setupButtons() {
        buttonRect = new ToggleButton("Rect");
        buttonRect.setGraphic(rect);

        buttonSquare = new ToggleButton("Square");
        buttonSquare.setGraphic(square);

        buttonCircle = new ToggleButton("Circle");
        buttonCircle.setGraphic(circle);

        buttonOval = new ToggleButton("Oval");
        buttonOval.setGraphic(oval);

        buttonLine = new ToggleButton("Line");
        buttonLine.setGraphic(line);

        buttons = new ToggleButton[]{buttonRect, buttonSquare, buttonCircle, buttonOval, buttonLine};

        // set up properties for each button
        for (ToggleButton b : buttons) {
            b.setMinWidth(100);
            b.setMaxWidth(100);
            b.setMaxHeight(Double.MAX_VALUE);
            this.getChildren().add(b);
            setVgrow(b, Priority.ALWAYS);
        }

        // Initial selection of the default button
        buttonSquare.setSelected(true);
        buttonSquare.setEffect(new DropShadow(25, Color.AQUA));
        square.setFill(Color.AQUA);
        selectedButton = buttonSquare;
        selectedShape = square;
    }

    /**
     * Method to set up an interactive model - as part of the MVC architecture.
     *
     * @param iModel : The iModel for this view.
     */
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    /**
     * Method to trigger actions when the buttons are pressed. Actions are routed through the controller without storing
     * the controller as an instance variable.
     *
     * @param controller : The controller for this view.
     */
    public void setController(DrawingController controller) {
        buttonRect.setOnAction(e -> {
            controller.setNewShape(0);
            selectedButton = buttonRect;
            selectedShape = rect;
            changeSelection();
        });
        buttonSquare.setOnAction(e -> {
            controller.setNewShape(1);
            selectedButton = buttonSquare;
            selectedShape = square;
            changeSelection();
        });
        buttonCircle.setOnAction(e -> {
            controller.setNewShape(2);
            selectedButton = buttonCircle;
            selectedShape = circle;
            changeSelection();
        });
        buttonOval.setOnAction(e -> {
            controller.setNewShape(3);
            selectedButton = buttonOval;
            selectedShape = oval;
            changeSelection();
        });
        buttonLine.setOnAction(e -> {
            controller.setNewShape(4);
            selectedButton = buttonLine;
            selectedShape = line;
            changeSelection();
        });
    }

    /**
     * Helper method to change the selected button and provide feedback to the user by filling the shape and adding a
     * drop shadow to the selected shape.
     */
    private void changeSelection() {
        for (ToggleButton b : buttons) {
            if (b == selectedButton) {
                b.setEffect(new DropShadow(25, (Color) iModel.getCurrentColour()));
            } else {
                b.setSelected(false);
                b.setEffect(null);
            }
        }
        for (Shape s : shapes) {
            s.setFill(Color.BLACK);
            s.setStroke(Color.BLACK);
        }
        selectedShape.setStroke(iModel.getCurrentColour());
        selectedShape.setFill(iModel.getCurrentColour());
    }

    /**
     * Method for receiving publish notifications from iModel. Part of publisher-subscriber model.
     */
    @Override
    public void iModelChanged() {
        // Change the colour on the selected button if required
        if (selectedShape != null && selectedButton != null) {
            changeSelection();
        }
    }
}
