package com.oleksii.paint.tools;

import com.oleksii.paint.PaintController;
import javafx.scene.canvas.GraphicsContext;

public class PenTool implements Tool {

    private double lastX, lastY;
    private PaintController controller;

    // Konstruktor s odkazem na controller
    public PenTool(PaintController controller) {
        this.controller = controller;
    }

    @Override
    public void onPress(GraphicsContext gc, double x, double y) {
        lastX = x;
        lastY = y;
        gc.setStroke(controller.getCurrentColor());
        gc.setLineWidth(controller.getBrushSize());
    }

    @Override
    public void onDrag(GraphicsContext gc, double x, double y) {
        gc.setStroke(controller.getCurrentColor());
        gc.setLineWidth(controller.getBrushSize());
        gc.strokeLine(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
    }

    @Override
    public void onRelease(GraphicsContext gc, double x, double y) {
        // nic speciálního
    }
}

