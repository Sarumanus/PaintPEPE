module com.tom.paint {
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.swing;
    requires javafx.controls;

    opens com.oleksii.paint to javafx.fxml;
    exports com.oleksii.paint;
}