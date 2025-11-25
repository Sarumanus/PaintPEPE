package com.oleksii.paint.tools;

import com.oleksii.paint.PaintController;
import javafx.scene.canvas.GraphicsContext;

public class HighlighterTool implements Tool {
    private double lastX, lastY;
    private final PaintController controller;

    public HighlighterTool(PaintController controller) {
        this.controller = controller;
    }

    @Override
    public void onPress(GraphicsContext gc, double x, double y) {
        lastX = x;
        lastY = y;
        gc.setStroke(controller.getCurrentColor().deriveColor(0, 1, 1, 0.3)); // průhlednost
        gc.setLineWidth(controller.getBrushSize() * 2); // silnější
    }

    @Override
    public void onDrag(GraphicsContext gc, double x, double y) {
        gc.setStroke(controller.getCurrentColor().deriveColor(0, 1, 1, 0.3));
        gc.setLineWidth(controller.getBrushSize() * 2);
        gc.strokeLine(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
    }

    @Override
    public void onRelease(GraphicsContext gc, double x, double y) {}
}

