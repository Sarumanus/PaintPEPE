package com.oleksii.paint.functions;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Rozhraní pro efekty, které upravují celý canvas.
 * (např. rozostření, negativ, inverze barev, zjasnění atd.)
 */
public interface Function {

    /**
     * Aplikuje efekt na celý canvas.
     *
     * @param canvas plátno, jehož obsah se má upravit
     * @param gc GraphicsContext pro kreslení
     */
    void apply(Canvas canvas, GraphicsContext gc);

    /**
     * Název efektu (pro ComboBox, menu apod.)
     *
     * @return čitelný název efektu
     */
    String getName();

    /**
     * Volitelně: náhled efektu (malý obrázek)
     * – můžeš použít třeba v GUI vedle názvu
     */
    default Image getPreviewImage() {
        return null;
    }
}
