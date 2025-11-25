package com.oleksii.paint;

import com.oleksii.paint.functions.*;
import com.oleksii.paint.tools.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class PaintController {

    // --- UI prvky ---
    @FXML private Canvas canvas;
    @FXML private ColorPicker colorPicker;
    @FXML private Slider brushSize;
    @FXML private ComboBox<String> toolBox;
    @FXML private ComboBox<String> extraBox;

    // --- Kreslení ---
    private GraphicsContext gc;
    private Tool currentTool;

    // --- Historie ---
    private final Stack<Image> undoStack = new Stack<>();
    private final Stack<Image> redoStack = new Stack<>();
    private static final int HISTORY_LIMIT = 20;

    // =========================================================
    // INIT
    // =========================================================
    @FXML
    private void initialize() {
        setupUI();
        setupCanvas();
        setupShortcuts();
    }

    private void setupUI() {
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        colorPicker.setValue(Color.BLACK);

        // Výchozí možnosti
        toolBox.getSelectionModel().select("Štětec");
        extraBox.getItems().add("Nástroje");
        extraBox.getSelectionModel().select("Nástroje");

        currentTool = new PenTool(this);

        // --- Reakce na změnu nástrojů ---
        toolBox.setOnAction(e -> handleToolChange(toolBox, extraBox));
        extraBox.setOnAction(e -> handleToolChange(extraBox, toolBox));

        saveState();
    }

    private void setupCanvas() {
        canvas.setOnMousePressed(e -> currentTool.onPress(gc, e.getX(), e.getY()));
        canvas.setOnMouseDragged(e -> currentTool.onDrag(gc, e.getX(), e.getY()));
        canvas.setOnMouseReleased(e -> {
            currentTool.onRelease(gc, e.getX(), e.getY());
            saveState();
        });
    }

    private void setupShortcuts() {
        canvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.isControlDown()) {
                        switch (event.getCode()) {
                            case Z -> undo();
                            case Y -> redo();
                        }
                    }
                });
            }
        });
    }

    // =========================================================
    // HISTORIE
    // =========================================================
    private void saveState() {
        if (undoStack.size() >= HISTORY_LIMIT) undoStack.remove(0);

        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);

        undoStack.push(snapshot);
        redoStack.clear();
    }

    @FXML
    private void undo() {
        if (undoStack.size() > 1) {
            Image last = undoStack.pop();
            redoStack.push(last);
            gc.drawImage(undoStack.peek(), 0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    @FXML
    private void redo() {
        if (!redoStack.isEmpty()) {
            Image next = redoStack.pop();
            undoStack.push(next);
            gc.drawImage(next, 0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    // =========================================================
    // CANVAS OVLÁDÁNÍ
    // =========================================================
    @FXML
    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        saveState();
    }

    public void redrawCanvas() {
        if (!undoStack.isEmpty()) {
            Image img = undoStack.peek();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    // =========================================================
    // SOUBORY
    // =========================================================
    @FXML
    private void saveImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Uložit obrázek");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg")
        );
        chooser.setInitialFileName("obrazek.png");

        File file = chooser.showSaveDialog(canvas.getScene().getWindow());
        if (file == null) return;

        try {
            WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(null, snapshot);
            BufferedImage img = SwingFXUtils.fromFXImage(snapshot, null);

            String ext = file.getName().toLowerCase().endsWith(".jpg") ? "jpg" : "png";
            ImageIO.write(img, ext, file);
            System.out.println("Uloženo: " + file.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void loadImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Otevřít obrázek");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Obrázky", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = chooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) return;

        try {
            Image img = new Image(file.toURI().toString());
            gc.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
            saveState();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =========================================================
    // INFO OKNO
    // =========================================================
    @FXML
    private void showInfoWindow() {
        Stage stage = new Stage();
        stage.setTitle("O aplikaci");

        Label info = new Label("""
                About:
                App version: 1.2.8
                Owner: Ukrajinec
                Tester & Consultant: Ondřej Rousek
                """);

        Button close = new Button("Zavřít");
        close.setOnAction(e -> stage.close());

        VBox box = new VBox(15, info, close);
        box.setAlignment(Pos.CENTER);

        Scene scene = new Scene(box, 300, 200);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(canvas.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    // =========================================================
    // EFEKTY
    // =========================================================
    @FXML
    private void applyEffect(javafx.event.ActionEvent e) {
        MenuItem item = (MenuItem) e.getSource();
        String key = (String) item.getUserData();

        Function f = switch (key) {
            case "Contrast" -> new ContrastFunction(0.5);
            case "Sepia" -> new SepiaFunction(8);
            case "Posun" -> new SaturationFunction(0.3);
            case "Inverze" -> new InvertColorsFunction();
            default -> null;
        };

        if (f != null) {
            f.apply(canvas, gc);
            saveState();
            System.out.println("Efekt použit: " + f.getName());
        }
    }

    // =========================================================
    // NÁSTROJE
    // =========================================================
    private void handleToolChange(ComboBox<String> source, ComboBox<String> other) {
        String selected = source.getValue();
        if (selected == null) return;

        // synchronizace mezi comboboxy
        if (!other.getItems().contains("Štětce")) other.getItems().add("Štětce");
        if (!source.getItems().contains("Nástroje")) source.getItems().add("Nástroje");

        switchTool(selected);
    }

    private void switchTool(String name) {
        currentTool = switch (name) {
            case "Štětec" -> new PenTool(this);
            case "Sprej" -> new SprayTool(this);
            case "Zvýrazňovač" -> new HighlighterTool(this);
            case "Kaligrafie" -> new CalligraphyTool(this);
            case "Hvězda" -> new StarBrushTool(this);
            case "Guma" -> new EraserTool(this);
            case "Kapátko" -> new EyedropperTool(this);
            case "Lupa" -> new MagnifierTool(this);
            default -> currentTool;
        };
        System.out.println("Aktivní nástroj: " + name);
    }

    // =========================================================
    // GETTERY / SETTERY
    // =========================================================
    public Color getCurrentColor() {
        return colorPicker.getValue();
    }

    public int getBrushSize() {
        return (int) Math.round(brushSize.getValue());
    }

    public WritableImage getCanvasSnapshot() {
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);
        return snapshot;
    }

    public void setCurrentColor(Color color) {
        colorPicker.setValue(color);
    }

    // =========================================================
    // UKONČENÍ
    // =========================================================
    @FXML
    private void exitApp() {
        ((Stage) canvas.getScene().getWindow()).close();
    }
}
