package com.oleksii.paint.tools;

import javafx.scene.canvas.GraphicsContext;

public interface Tool {
    void onPress(GraphicsContext gc, double x, double y);
    void onDrag(GraphicsContext gc, double x, double y);
    void onRelease(GraphicsContext gc, double x, double y);
}
