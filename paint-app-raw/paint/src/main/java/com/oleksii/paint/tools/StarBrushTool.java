package com.oleksii.paint.tools;

import com.oleksii.paint.PaintController;
import javafx.scene.canvas.GraphicsContext;

public class StarBrushTool implements Tool {
    private final PaintController controller;

    public StarBrushTool(PaintController controller) {
        this.controller = controller;
    }

    @Override
    public void onPress(GraphicsContext gc, double x, double y) {
        drawStar(gc, x, y);
    }

    @Override
    public void onDrag(GraphicsContext gc, double x, double y) {
        drawStar(gc, x, y);
    }

    @Override
    public void onRelease(GraphicsContext gc, double x, double y) {}

    private void drawStar(GraphicsContext gc, double x, double y) {
        gc.setFill(controller.getCurrentColor());
        int size = (int) controller.getBrushSize() * 4;

        double[] xPoints = new double[10];
        double[] yPoints = new double[10];

        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i;
            double radius = (i % 2 == 0) ? size : size / 2.0;
            xPoints[i] = x + Math.cos(angle) * radius;
            yPoints[i] = y + Math.sin(angle) * radius;
        }

        gc.fillPolygon(xPoints, yPoints, 10);
    }
}
