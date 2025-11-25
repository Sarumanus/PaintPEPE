package com.oleksii.paint.functions;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;

public class ContrastFunction implements Function {

    private final double contrast; // -1.0 až 1.0

    public ContrastFunction(double contrast) {
        this.contrast = Math.max(-1, Math.min(contrast, 1));
    }

    @Override
    public void apply(Canvas canvas, GraphicsContext gc) {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setContrast(contrast);
        gc.applyEffect(adjust);
    }

    @Override
    public String getName() {
        return "Kontrast";
    }
}
