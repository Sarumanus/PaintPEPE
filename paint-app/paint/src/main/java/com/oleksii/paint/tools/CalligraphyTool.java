package com.oleksii.paint.tools;

import com.oleksii.paint.PaintController;
import javafx.scene.canvas.GraphicsContext;

public class CalligraphyTool implements Tool {

    private double lastX, lastY;
    private PaintController controller;

    // úhel štětce (např. 45° pro kaligrafii)
    private double angle = Math.toRadians(45);

    public CalligraphyTool(PaintController controller) {
        this.controller = controller;
    }

    @Override
    public void onPress(GraphicsContext gc, double x, double y) {
        lastX = x;
        lastY = y;
    }

    @Override
    public void onDrag(GraphicsContext gc, double x, double y) {
        gc.setFill(controller.getCurrentColor());

        double brushWidth = controller.getBrushSize() * 2;   // šířka štětce
        double brushHeight = controller.getBrushSize() * 0.5; // výška štětce (plošší = kaligrafický efekt)

        double dx = x - lastX;
        double dy = y - lastY;
        double distance = Math.hypot(dx, dy);

        // vykresli "otisky" podél cesty
        int steps = (int) distance;
        for (int i = 0; i < steps; i++) {
            double t = (double) i / steps;
            double drawX = lastX + dx * t;
            double drawY = lastY + dy * t;

            gc.save();
            gc.translate(drawX, drawY);
            gc.rotate(Math.toDegrees(angle));
            gc.fillOval(-brushWidth / 2, -brushHeight / 2, brushWidth, brushHeight);
            gc.restore();
        }

        lastX = x;
        lastY = y;
    }

    @Override
    public void onRelease(GraphicsContext gc, double x, double y) {
        // nic speciálního
    }
}
