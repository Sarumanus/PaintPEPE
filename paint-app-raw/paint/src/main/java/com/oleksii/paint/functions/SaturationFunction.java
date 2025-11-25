package com.oleksii.paint.functions;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;

public class SaturationFunction implements Function {

    private final double saturation; // -1.0 až 1.0

    public SaturationFunction(double saturation) {
        this.saturation = Math.max(-1, Math.min(saturation, 1));
    }

    @Override
    public void apply(Canvas canvas, GraphicsContext gc) {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setSaturation(saturation);
        gc.applyEffect(adjust);
    }

    @Override
    public String getName() {
        return "Sytost";
    }
}
