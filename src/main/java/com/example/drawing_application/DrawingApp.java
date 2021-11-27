package com.example.drawing_application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application class for a basic drawing application. Allows the user to select a shape, a colour and draw that shape
 * on to the center canvas. It also displays a mini-view of the entire canvas and supports panning via right click or
 * through moving the view-finder in the mini-view.
 */
public class DrawingApp extends Application {
    /**
     * Main method that runs when this application is started by Java.
     *
     * @param args : N/A.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Method to set up and start the various parts of the drawing application.
     *
     * @param stage : Top-level JavaFX container.
     */
    @Override
    public void start(Stage stage) {
        // Setup Model
        DrawingModel model = new DrawingModel();
        InteractionModel iModel = new InteractionModel();

        // Setup View
        MainUI mainUI = new MainUI();
        ShapeToolbar shapeToolbar = new ShapeToolbar();
        ColourToolbar colourToolbar = new ColourToolbar();
        DrawingView drawingView = new DrawingView(2000, 2000, 500, 500);
        MiniDrawingView miniDrawingView = new MiniDrawingView(2000, 2000, 100, 100);

        // Setup Controllers
        DrawingController controller = new DrawingController();
        DrawingController miniController = new MiniDrawingController();

        // Setup MVC architecture
        // Connect the DrawingView
        drawingView.setModel(model);
        drawingView.setIModel(iModel);
        drawingView.setController(controller);

        // Connect the Mini Drawing View
        miniDrawingView.setModel(model);
        miniDrawingView.setIModel(iModel);
        miniDrawingView.setController(miniController);

        // Connect the Shape Toolbar
        shapeToolbar.setIModel(iModel);
        shapeToolbar.setController(controller);

        // Connect the Colour Toolbar
        colourToolbar.setIModel(iModel);
        colourToolbar.setController(controller);

        // Connect the Controllers
        controller.setModel(model);
        controller.setIModel(iModel);
        miniController.setModel(model);
        miniController.setIModel(iModel);

        // Add all views to the MainUI
        mainUI.setUI(shapeToolbar, drawingView, miniDrawingView, colourToolbar);

        // Connect subscribers to Model
        model.addSubscriber(drawingView);
        model.addSubscriber(miniDrawingView);

        // Connect subscribers to iModel
        iModel.addISubscriber(drawingView);
        iModel.addISubscriber(shapeToolbar);
        iModel.addISubscriber(miniDrawingView);

        // Set up and start the primary stage
        Scene scene = new Scene(mainUI, 700, 500);
        scene.setOnKeyPressed(controller::handleKeyPressed);
        stage.setTitle("Drawing App");
        stage.setScene(scene);
        stage.show();
    }
}