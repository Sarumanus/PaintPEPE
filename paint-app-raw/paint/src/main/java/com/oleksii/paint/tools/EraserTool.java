package com.oleksii.paint.tools;

import com.oleksii.paint.PaintController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EraserTool implements Tool {

    private double lastX, lastY;
    private PaintController controller;

    public EraserTool(PaintController controller) {
        this.controller = controller;
    }

    @Override
    public void onPress(GraphicsContext gc, double x, double y) {
        lastX = x;
        lastY = y;
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(controller.getBrushSize() * 5); // guma je silnější
    }

    @Override
    public void onDrag(GraphicsContext gc, double x, double y) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(controller.getBrushSize() * 5);
        gc.strokeLine(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
    }

    @Override
    public void onRelease(GraphicsContext gc, double x, double y) {
        // nic speciálního
    }
}
