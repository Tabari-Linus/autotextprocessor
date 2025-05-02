package lii.autotexttprocessor.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lii.autotexttprocessor.util.LoggerUtil;

public class DashboardController {
    private GridPane mainContainer;
    private TabPane tabPane;

    public DashboardController(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public GridPane getView() {
        if (mainContainer == null) {
            initializeUI();
        }
        return mainContainer;
    }

    private void initializeUI() {
        mainContainer = new GridPane();
        mainContainer.setPadding(new Insets(20));
        mainContainer.setHgap(20);
        mainContainer.setVgap(20);
        mainContainer.setAlignment(Pos.CENTER);

        // Create dashboard cards
        VBox regexCard = createDashboardCard("Regex Tools", "Perform powerful pattern matching and text manipulation", "primary", 0);
        VBox analysisCard = createDashboardCard("Text Analysis", "Analyze text for word frequency, statistics and more", "success", 1);
        VBox fileCard = createDashboardCard("File Processing", "Batch process multiple files with regex operations", "warning", 2);
        VBox dataCard = createDashboardCard("Data Management", "Manage your data entries with CRUD operations", "danger", 3);
        // Add cards to grid
        mainContainer.add(regexCard, 0, 0);
        mainContainer.add(analysisCard, 1, 0);
        mainContainer.add(fileCard, 0, 1);
        mainContainer.add(dataCard, 1, 1);
    }

    private VBox createDashboardCard(String title, String description, String styleClass, int tabIndex) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(300);
        card.setMinHeight(200);

        // Add styling based on type
        String bgColor = switch (styleClass) {
            case "primary" -> "#3498db";
            case "success" -> "#2ecc71";
            case "warning" -> "#f39c12";
            case "danger" -> "#e74c3c";
            default -> "#95a5a6";
        };

        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 10;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font(20));
        titleLabel.setTextFill(Color.WHITE);

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setTextFill(Color.WHITE);
        descLabel.setAlignment(Pos.CENTER);

        card.getChildren().addAll(titleLabel, descLabel);

        // Add hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: derive(" + bgColor + ", -20%); -fx-background-radius: 10; -fx-cursor: hand;");
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 10;");
        });

        card.setOnMouseClicked((MouseEvent event) -> {
            tabPane.getSelectionModel().select(tabIndex);
        });
        return card;
    }
}