package com.oleksii.paint.functions;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.SepiaTone;

public class SepiaFunction implements Function {

    private final double level; // 0.0 - 1.0

    public SepiaFunction(double level) {
        this.level = Math.max(0, Math.min(level, 1)); // omezíme rozsah
    }

    @Override
    public void apply(Canvas canvas, GraphicsContext gc) {
        SepiaTone sepia = new SepiaTone(level);
        gc.applyEffect(sepia);
    }

    @Override
    public String getName() {
        return "Sépie";
    }
}
