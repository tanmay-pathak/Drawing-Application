package com.example.drawing_application;

import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Class that creates a toolbar with 8 buttons representing different colours. Colours in the toolbar in-order are:
 * Aqua, Violet, Green, Gold, Orange, Coral, Fuchsia and Peru.
 */
public class ColourToolbar extends VBox {
    /*
        Instance variables to store interactive model, buttons and a list as a container for the buttons.
     */
    InteractionModel iModel;
    ToggleButton buttonAqua, buttonViolet, buttonGreen, buttonGold, buttonOrange, buttonCoral, buttonFuchsia, buttonPeru;
    ToggleButton[] buttons;
    ToggleButton selectedButton;

    /**
     * Default constructor for ColourToolbar class which sets up all the buttons.
     */
    public ColourToolbar() {
        super();
        setupButton();
    }

    /**
     * Helper method to set up all the buttons with respective colour fills.
     */
    private void setupButton() {
        buttonAqua = new ToggleButton("Aqua");
        buttonAqua.setStyle("-fx-background-color: #00FFFF;");

        buttonViolet = new ToggleButton("Violet");
        buttonViolet.setStyle("-fx-background-color: #8F00FF;");

        buttonGreen = new ToggleButton("Green");
        buttonGreen.setStyle("-fx-background-color: #00FF00;");

        buttonGold = new ToggleButton("Gold");
        buttonGold.setStyle("-fx-background-color: #FFD700;");

        buttonOrange = new ToggleButton("Orange");
        buttonOrange.setStyle("-fx-background-color: #FFA500;");

        buttonCoral = new ToggleButton("Coral");
        buttonCoral.setStyle("-fx-background-color: #FF7F50;");

        buttonFuchsia = new ToggleButton("Fuchsia");
        buttonFuchsia.setStyle("-fx-background-color: #ca2c92;");

        buttonPeru = new ToggleButton("Peru");
        buttonPeru.setStyle("-fx-background-color: #CD853F;");

        // Setting properties for each button
        buttons = new ToggleButton[]{buttonAqua, buttonViolet, buttonGreen, buttonGold, buttonOrange, buttonCoral, buttonFuchsia, buttonPeru};
        for (ToggleButton b : buttons) {
            b.setMaxWidth(70);
            b.setMaxHeight(Double.MAX_VALUE);
            this.getChildren().add(b);
            setVgrow(b, Priority.ALWAYS);
        }

        // Initial selection of the default button
        buttonAqua.setSelected(true);
        buttonAqua.setEffect(new DropShadow(25, Color.AQUA));
        selectedButton = buttonAqua;
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
        /*
            For each button, the action to be performed is of changing the current colour in the iModel via the Controller.
            Also provide feedback to the user that the button is pressed which is handled by changeSelection().
         */
        buttonAqua.setOnAction(e -> {
            controller.setCurrentColour(Color.AQUA);
            selectedButton = buttonAqua;
            changeSelection();
        });
        buttonViolet.setOnAction(e -> {
            controller.setCurrentColour(Color.VIOLET);
            selectedButton = buttonViolet;
            changeSelection();
        });
        buttonGreen.setOnAction(e -> {
            controller.setCurrentColour(Color.GREEN);
            selectedButton = buttonGreen;
            changeSelection();
        });
        buttonGold.setOnAction(e -> {
            controller.setCurrentColour(Color.GOLD);
            selectedButton = buttonGold;
            changeSelection();
        });
        buttonOrange.setOnAction(e -> {
            controller.setCurrentColour(Color.ORANGE);
            selectedButton = buttonOrange;
            changeSelection();
        });
        buttonCoral.setOnAction(e -> {
            controller.setCurrentColour(Color.CORAL);
            selectedButton = buttonCoral;
            changeSelection();
        });
        buttonFuchsia.setOnAction(e -> {
            controller.setCurrentColour(Color.FUCHSIA);
            selectedButton = buttonFuchsia;
            changeSelection();
        });
        buttonPeru.setOnAction(e -> {
            controller.setCurrentColour(Color.PERU);
            selectedButton = buttonPeru;
            changeSelection();
        });
    }

    /**
     * Helper method to provide feedback to the user that a button has been pressed. Adds the effect of a drop shadow
     * to the button of the colour selected.
     */
    private void changeSelection() {
        // Add shadow to the selected button | Reset the others
        for (ToggleButton b : buttons) {
            if (b == selectedButton) {
                b.setEffect(new DropShadow(25, (Color) iModel.getCurrentColour()));
            } else {
                b.setSelected(false);
                b.setEffect(null);
            }
        }
    }
}