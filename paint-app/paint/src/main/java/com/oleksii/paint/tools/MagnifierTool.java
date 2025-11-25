package com.oleksii.paint.tools;

import com.oleksii.paint.PaintController;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

/**
 * Nástroj Lupa – při držení myši zobrazuje zvětšený náhled oblasti pod kurzorem,
 * který se plynule posouvá, reaguje na kolečko myši (zoom)
 * a vykresluje jemné čáry oddělující pixely.
 */
public class MagnifierTool implements Tool {

    private final PaintController controller;
    private boolean active = false;

    private double radius = 50;          // velikost kruhu lupy
    private double zoomFactor = 3.0;     // míra zvětšení
    private final double minZoom = 1.0;  // minimální zoom
    private final double maxZoom = 10.0; // maximální zoom

    private WritableImage snapshot;      // uložený obraz plátna
    private double lastX, lastY;         // poslední pozice kurzoru

    public MagnifierTool(PaintController controller) {
        this.controller = controller;
    }

    @Override
    public void onPress(GraphicsContext gc, double x, double y) {
        active = true;
        lastX = x;
        lastY = y;

        Canvas canvas = gc.getCanvas();
        snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.snapshot(params, snapshot);

        // Přidáme posluchač kolečka myši (jen jednou)
        canvas.addEventHandler(ScrollEvent.SCROLL, e -> {
            if (active) {
                double delta = e.getDeltaY() > 0 ? 0.2 : -0.2;
                zoomFactor = Math.max(minZoom, Math.min(maxZoom, zoomFactor + delta));
                controller.redrawCanvas();
                drawMagnifiedArea(gc, lastX, lastY);
                e.consume();
            }
        });

        drawMagnifiedArea(gc, x, y);
    }

    @Override
    public void onDrag(GraphicsContext gc, double x, double y) {
        if (active && snapshot != null) {
            lastX = x;
            lastY = y;
            controller.redrawCanvas();
            drawMagnifiedArea(gc, x, y);
        }
    }

    @Override
    public void onRelease(GraphicsContext gc, double x, double y) {
        active = false;
        snapshot = null;
        controller.redrawCanvas();
    }

    /**
     * Vykreslí lupu s přiblíženou oblastí okolo kurzoru.
     */
    private void drawMagnifiedArea(GraphicsContext gc, double x, double y) {
        if (snapshot == null) return;

        int size = (int) (radius * 2);
        double sx = Math.max(0, x - radius);
        double sy = Math.max(0, y - radius);
        double ex = Math.min(snapshot.getWidth(), sx + size);
        double ey = Math.min(snapshot.getHeight(), sy + size);

        int subWidth = (int) Math.ceil(ex - sx);
        int subHeight = (int) Math.ceil(ey - sy);

        WritableImage sub = new WritableImage(snapshot.getPixelReader(),
                (int) sx, (int) sy, subWidth, subHeight);

        gc.save();

        // Oříznutí do kruhu
        gc.beginPath();
        gc.arc(x, y, radius, radius, 0, 360);
        gc.closePath();
        gc.clip();

        // Vykreslení zvětšené části
        double drawX = x - subWidth * zoomFactor / 2;
        double drawY = y - subHeight * zoomFactor / 2;
        double drawW = subWidth * zoomFactor;
        double drawH = subHeight * zoomFactor;

        gc.drawImage(sub, drawX, drawY, drawW, drawH);

        // --- Přesná pixelová mřížka ---
        if (zoomFactor >= 3.0) {
            gc.setStroke(Color.color(0, 0, 0, 0.5));
            gc.setLineWidth(1.0 / zoomFactor); // tloušťka úměrná přiblížení (zabrání „rozmazání“)

            // přesné zarovnání – každá čára přesně mezi pixely
            for (int i = 0; i <= subWidth; i++) {
                double lineX = drawX + i * zoomFactor + 0.5;
                gc.strokeLine(lineX, drawY, lineX, drawY + drawH);
            }
            for (int j = 0; j <= subHeight; j++) {
                double lineY = drawY + j * zoomFactor + 0.5;
                gc.strokeLine(drawX, lineY, drawX + drawW, lineY);
            }
        }

        gc.restore();

        // Kruh kolem lupy
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);

        // Zoom label
        gc.setFill(Color.color(0, 0, 0, 0.6));
        gc.fillRect(x - radius + 4, y + radius - 20, 45, 16);
        gc.setFill(Color.WHITE);
        gc.fillText("x" + String.format("%.1f", zoomFactor), x - radius + 10, y + radius - 8);
    }
}
