package com.example.drawing_application;

import javafx.scene.paint.Color;

/**
 * View class for the mini-view that has the canvas for the user to draw. Subscribes to model and interactive model for changes.
 * An extension of the main Drawing View. Adds the functionality to draw the view-finder rectangle.
 */
public class MiniDrawingView extends DrawingView {
    /**
     * Default constructor for this class. Calls super's constructor and sets the size of the document to draw the full
     * canvas.
     *
     * @param docW       : document width
     * @param docH       : document height
     * @param viewWidth  : view's width
     * @param viewHeight : view's height
     */
    public MiniDrawingView(double docW, double docH, double viewWidth, double viewHeight) {
        super(docW, docH, viewWidth, viewHeight);
        this.setMaxHeight(viewHeight);
        this.setMaxWidth(viewWidth);
        this.docWidth = viewWidth;
        this.docHeight = viewHeight;
    }

    /**
     * Over-ridden draw method to add the functionality of drawing the view-finder.
     */
    @Override
    protected void draw() {
        super.draw();
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);
        gc.strokeRect(iModel.getViewLeft() * docWidth, iModel.getViewTop() * docHeight, iModel.getViewFinderWidth() * docWidth, iModel.getViewFinderHeight() * docHeight);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
    }

    /**
     * Over-ridden helper method to calculate the de-normalized coordinates. Mini-view draws all the shapes on the canvas
     * therefore does not need the values to be adjusted based on the view-port.
     *
     * @param shape : shape for which to de-normalize location and size
     */
    @Override
    protected void deNormalize(XShape shape) {
        shapeLeft = (shape.getLeft()) * docWidth;
        shapeTop = (shape.getTop()) * docHeight;
        shapeWidth = shape.getWidth() * docWidth;
        shapeHeight = shape.getHeight() * docHeight;
    }

    /**
     * Over-ridden helper method to calculate the de-normalized coordinates. Mini-view draws all the shapes on the canvas
     * therefore does not need the values to be adjusted based on the view-port.
     *
     * @param shape : line shape to denormalize the coordinates for
     */
    @Override
    protected void deNormalizeLine(XShape shape) {
        deNormalize(shape);
    }

    /**
     * Over-ridden helper method to draw the bounding box for a selected shape. Mini-view uses solid lines instead of
     * dashed-lines used by the main drawing view.
     *
     * @param shape : shape for which the bounding box is to be drawn
     */
    @Override
    protected void drawBoundingBox(XShape shape) {
        deNormalize(shape);
        gc.setStroke(Color.RED);
        if (!(shape instanceof XLine)) {
            gc.strokeRect(shapeLeft, shapeTop, shapeWidth, shapeHeight);
        } else {
            gc.strokeLine(shapeLeft, shapeTop, shapeWidth, shapeHeight);
        }
        gc.setStroke(Color.BLACK);
    }
}