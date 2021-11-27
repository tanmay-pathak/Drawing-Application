package com.example.drawing_application;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * View class that has the canvas for the user to draw. Subscribes to model and interactive model for changes.
 */
public class DrawingView extends StackPane implements ModelSubscriber, InteractionModelSubscriber {
    /*
        Instance variables to store the canvas, graphic context, model, iModel and other variables to assist with the
        drawing of shapes.
     */
    Canvas myCanvas;
    GraphicsContext gc;
    DrawingModel model;
    InteractionModel iModel;
    // canvas/document size
    double docWidth, docHeight;
    double shapeLeft, shapeTop, shapeWidth, shapeHeight;

    /**
     * Default constructor for this class. Sets up the canvas/document for the user to interact with. Draw shapes based
     * on the view-port.
     *
     * @param docW       : document width
     * @param docH       : document height
     * @param viewWidth  : view-port width
     * @param viewHeight : view-port height
     */
    public DrawingView(double docW, double docH, double viewWidth, double viewHeight) {
        myCanvas = new ResizableCanvas(viewWidth, viewHeight, this);
        gc = myCanvas.getGraphicsContext2D();
        getChildren().add(myCanvas);
        docWidth = docW;
        docHeight = docH;
    }

    /**
     * Method to set up the model - as part of the MVC architecture.
     *
     * @param model : The model for this view.
     */
    public void setModel(DrawingModel model) {
        this.model = model;
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
     * Method to pass on mouse events to the controller. The coordinates are passed on as normalized coordinates.
     *
     * @param controller : controller that handles all the communication between this view and model/iModel
     */
    public void setController(DrawingController controller) {
        myCanvas.setOnMousePressed(e -> controller.handlePressed(e.getX() / docWidth, e.getY() / docHeight, e));
        myCanvas.setOnMouseDragged(e -> controller.handleMove(e.getX() / docWidth, e.getY() / docHeight, e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e.getX() / docWidth, e.getY() / docHeight, e));

        // update view-port width and height when the window size changes
        this.widthProperty().addListener((observable, oldVal, newVal) -> {
            double temp = newVal.doubleValue() / docWidth;
            if (temp == 1) {
                temp = 0.25;
            }
            controller.setViewFinderWidth(temp);
        });
        this.heightProperty().addListener((observable, oldVal, newVal) -> {
            double temp = newVal.doubleValue() / docHeight;
            if (temp == 1) {
                temp = 0.25;
            }
            controller.setViewFinderHeight(temp);
        });
    }

    /**
     * Method for receiving publish notifications from model. Part of publisher-subscriber model.
     */
    @Override
    public void modelChanged() {
        draw();
    }

    /**
     * Method for receiving publish notifications from iModel. Part of publisher-subscriber model.
     */
    @Override
    public void iModelChanged() {
        draw();
    }

    /**
     * Method to draw various shapes from the model and the selected shape from iModel.
     */
    protected void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        gc.setStroke(Color.BLACK);
        model.getShapes().forEach(shape -> {
            // call helper method to draw according to the shape
            drawBasedOnShape(shape);
            if (shape == iModel.getSelectedShape()) {
                deNormalize(shape);
                drawBoundingBox(shape);
            }
        });
        // call helper method to draw current shape from iModel
        drawCurrentShape();
    }

    /**
     * Helper method to call different draw methods based on the shape given.
     *
     * @param shape : shape based on which to call draw method
     */
    private void drawBasedOnShape(XShape shape) {
        // helper method to denormalize coordinates based on the document width and height
        deNormalize(shape);
        switch (shape) {
            case XRectangle rectangle -> this.drawRectangle(rectangle);
            case XSquare square -> this.drawRectangle(square);
            case XCircle circle -> this.drawOval(circle);
            case XOval oval -> this.drawOval(oval);
            case XLine line -> this.drawLine(line);
            default -> throw new IllegalStateException("Unexpected value");
        }
    }

    /**
     * Helper method to de-normalize the shape's coordinates and size.
     *
     * @param shape : shape for which to de-normalize location and size
     */
    protected void deNormalize(XShape shape) {
        // adjust location based on the view-port and de-normalize
        shapeLeft = (shape.getLeft() - iModel.getViewLeft()) * docWidth;
        shapeTop = (shape.getTop() - iModel.getViewTop()) * docHeight;
        shapeWidth = shape.getWidth() * docWidth;
        shapeHeight = shape.getHeight() * docHeight;
        // line does not have width and height but has x,y coordinates of the end point. Therefore, de-norm that too.
        if (shape instanceof XLine) {
            shapeWidth -= iModel.getViewLeft() * docWidth;
            shapeHeight -= iModel.getViewTop() * docHeight;
        }
    }

    /**
     * Helper method to draw the current shape from iModel.
     */
    private void drawCurrentShape() {
        XShape currentShape = iModel.getSelectedShape();
        gc.setFill(currentShape.getColor());
        drawBasedOnShape(currentShape);
        deNormalize(currentShape);
        drawBoundingBox(currentShape);
    }

    /**
     * Helper method to draw the bounding box of a given shape.
     *
     * @param shape : shape for which the bounding box is to be drawn
     */
    protected void drawBoundingBox(XShape shape) {
        double[] dashPattern = {7, 7};
        gc.setStroke(Color.RED);
        gc.setLineWidth(3.0);
        gc.setLineDashes(dashPattern);

        // draw bounding box based on shape - line uses strokeLine()
        if (!(shape instanceof XLine)) {
            gc.strokeRect(shapeLeft, shapeTop, shapeWidth, shapeHeight);
        } else {
            gc.strokeLine(shapeLeft, shapeTop, shapeWidth, shapeHeight);
        }

        // reset the tools used to draw the bounding box
        gc.setStroke(Color.BLACK);
        gc.setLineDashes(null);
        gc.setLineWidth(1.0);

        // call helper method to draw the resize handle for non-temporary shapes
        if (shape.getWidth() != 0 && shape.getHeight() != 0 && shape.getHeight() != shape.getTop()) {
            drawResizeHandle(shape);
        }
    }

    /**
     * Helper method to draw the resize-handle for shapes.
     *
     * @param shape : shape for which the resize-handle is to be drawn
     */
    private void drawResizeHandle(XShape shape) {
        double resizeLeft, resizeTop, resizeWidth, resizeHeight;
        deNormalize(shape);

        // draw the handle using end of line x,y coordinates
        if (shape instanceof XLine) {
            resizeLeft = shapeWidth - (0.002 * docWidth);
            resizeTop = shapeHeight - (0.002 * docHeight);
        } else {
            // for other shapes, calculate the location
            resizeLeft = (shapeLeft + shapeWidth) - (0.003 * docWidth);
            resizeTop = (shapeTop + shapeHeight) - (0.003 * docHeight);
        }
        // size of the resize-handle circle
        resizeWidth = iModel.getResizeHandleSize() * docWidth;
        resizeHeight = iModel.getResizeHandleSize() * docHeight;

        // draw
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(resizeLeft, resizeTop, resizeWidth, resizeHeight);
        gc.fillOval(resizeLeft, resizeTop, resizeWidth, resizeHeight);
    }

    /**
     * Helper method to draw a line.
     *
     * @param shape : line shape
     */
    private void drawLine(XLine shape) {
        deNormalizeLine(shape);
        gc.setStroke(shape.getColor());
        gc.strokeLine(shapeLeft, shapeTop, shapeWidth, shapeHeight);
        gc.setStroke(Color.BLACK);
    }

    /**
     * Helper method to de-normalize coordinates for a line. Line does not store size so de-normalize start and end points.
     *
     * @param shape : line shape
     */
    protected void deNormalizeLine(XShape shape) {
        shapeLeft = (shape.getLeft() - iModel.getViewLeft()) * docWidth;
        shapeTop = (shape.getTop() - iModel.getViewTop()) * docHeight;
        shapeWidth = (shape.getWidth() - iModel.getViewLeft()) * docWidth;
        shapeHeight = (shape.getHeight() - iModel.getViewTop()) * docHeight;
    }

    /**
     * Helper method to draw an oval.
     *
     * @param shape : oval shape
     */
    private void drawOval(XShape shape) {
        gc.setFill(shape.getColor());
        gc.fillOval(shapeLeft, shapeTop, shapeWidth, shapeHeight);
        gc.strokeOval(shapeLeft, shapeTop, shapeWidth, shapeHeight);
    }

    /**
     * Helper method to draw a rectangle.
     *
     * @param shape : rectangle shape
     */
    private void drawRectangle(XShape shape) {
        gc.setFill(shape.getColor());
        gc.fillRect(shapeLeft, shapeTop, shapeWidth, shapeHeight);
        gc.strokeRect(shapeLeft, shapeTop, shapeWidth, shapeHeight);
    }

    /**
     * Inner-class extension of the Canvas to enable auto-resizing.
     */
    public static class ResizableCanvas extends Canvas {
        DrawingView view;

        /**
         * Default constructor for this class. Sets up the canvas and stores the view.
         *
         * @param w    : width of the canvas
         * @param h    : height of the canvas
         * @param view : view for which draw is to be called on resize
         */
        public ResizableCanvas(double w, double h, DrawingView view) {
            super(w, h);
            this.view = view;
        }

        @Override
        public double prefWidth(double width) {
            return 500;
        }

        @Override
        public double minWidth(double height) {
            return 100;
        }

        @Override
        public double maxWidth(double height) {
            return 10000;
        }

        @Override
        public double prefHeight(double width) {
            return 500;
        }

        @Override
        public double minHeight(double width) {
            return 100;
        }

        @Override
        public double maxHeight(double width) {
            return 10000;
        }

        @Override
        public boolean isResizable() {
            return true;
        }

        @Override
        public void resize(double width, double height) {
            super.setWidth(width);
            super.setHeight(height);
            view.draw();
        }
    }
}
