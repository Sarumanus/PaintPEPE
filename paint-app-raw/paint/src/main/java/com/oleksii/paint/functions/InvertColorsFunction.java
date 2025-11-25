package com.oleksii.paint.functions;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;

public class InvertColorsFunction implements Function {

    @Override
    public void apply(Canvas canvas, GraphicsContext gc) {
        ColorAdjust invert = new ColorAdjust();
        invert.setContrast(1.0);
        invert.setBrightness(0.0);
        invert.setHue(1.0); // posun barev
        gc.applyEffect(invert);
    }

    @Override
    public String getName() {
        return "Invertované barvy";
    }
}
