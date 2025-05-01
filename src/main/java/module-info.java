module lii.autotexttprocessor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens lii.autotexttprocessor to javafx.fxml;
    exports lii.autotexttprocessor;
    exports lii.autotexttprocessor.controller;
    opens lii.autotexttprocessor.controller to javafx.fxml;
    opens lii.autotexttprocessor.fxml to javafx.fxml;
}