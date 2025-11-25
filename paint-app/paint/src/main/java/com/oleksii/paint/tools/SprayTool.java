package com.oleksii.paint.tools;

import com.oleksii.paint.PaintController;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class SprayTool implements Tool {

    private double lastX, lastY;
    private PaintController controller;
    private Random random = new Random();

    public SprayTool(PaintController controller) {
        this.controller = controller;
    }

    @Override
    public void onPress(GraphicsContext gc, double x, double y) {
        lastX = x;
        lastY = y;
        spray(gc, x, y);
    }

    @Override
    public void onDrag(GraphicsContext gc, double x, double y) {
        spray(gc, x, y);
    }

    @Override
    public void onRelease(GraphicsContext gc, double x, double y) {
    }

    private void spray(GraphicsContext gc, double x, double y) {
        gc.setFill(controller.getCurrentColor());
        int density = 20; // počet teček
        double radius = controller.getBrushSize(); // rozsah spreje

        for (int i = 0; i < density; i++) {
            double offsetX = random.nextDouble() * radius * 2 - radius;
            double offsetY = random.nextDouble() * radius * 2 - radius;
            gc.fillOval(x + offsetX, y + offsetY, 1, 1);
        }
    }
}

