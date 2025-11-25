package com.oleksii.paint.tools;

import com.oleksii.paint.PaintController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EyedropperTool implements Tool {

    private PaintController controller;

    public EyedropperTool(PaintController controller) {
        this.controller = controller;
    }

    @Override
    public void onPress(GraphicsContext gc, double x, double y) {
        pickColor(x, y);
    }

    @Override
    public void onDrag(GraphicsContext gc, double x, double y) {
        pickColor(x, y);
    }

    @Override
    public void onRelease(GraphicsContext gc, double x, double y) {}

    private void pickColor(double x, double y) {
        try {
            int ix = (int) x;
            int iy = (int) y;

            // z canvasu přečte pixel
            Color color = controller.getCanvasSnapshot().getPixelReader().getColor(ix, iy);
            controller.setCurrentColor(color); // nastaví novou barvu do ColorPickeru
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
