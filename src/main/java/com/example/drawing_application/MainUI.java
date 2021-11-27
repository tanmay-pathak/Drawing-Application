package com.example.drawing_application;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * Class to organize and layout the different views used for the Drawing Application. Shape Toolbar, Drawing View, Mini
 * Drawing View and the Colour Toolbar.
 */
public class MainUI extends BorderPane {
    StackPane centerPane;

    /**
     * Default constructor for this class. Sets up a stack pane to lay out the drawing view and the mini view.
     */
    public MainUI() {
        super();
        centerPane = new StackPane();
    }

    /**
     * Method to set the UI and lay out all the different views.
     *
     * @param shapeToolbar  : shape toolbar to be placed on the left
     * @param drawingView   : main drawing view to be placed on the center
     * @param miniView      : mini drawing view to be placed on top left of the drawing view
     * @param colourToolbar : colour toolbar to be placed on the right
     */
    public void setUI(ShapeToolbar shapeToolbar, DrawingView drawingView, DrawingView miniView, ColourToolbar colourToolbar) {
        this.setLeft(shapeToolbar);
        this.setCenter(centerPane);
        this.setRight(colourToolbar);

        // set mini-drawing view's background as grey to differentiate it from the main drawing view
        miniView.setStyle("-fx-background-color: grey;");

        // add and lay out all components
        centerPane.getChildren().addAll(miniView, drawingView);
        StackPane.setAlignment(miniView, Pos.TOP_LEFT);
        StackPane.setAlignment(drawingView, Pos.CENTER);

        // push the mini view to the front to let it receive the mouse/keyboard events
        miniView.toFront();
    }
}
