package lii.autotexttprocessor.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lii.autotexttprocessor.model.DataEntry;
import lii.autotexttprocessor.service.DataManagementService;
import lii.autotexttprocessor.util.LoggerUtil;

public class DataManagementController {
    @FXML private TableView<DataEntry> dataTableView;
    @FXML private TableColumn<DataEntry, Integer> idColumn;
    @FXML private TableColumn<DataEntry, String> nameColumn;
    @FXML private TableColumn<DataEntry, String> valueColumn;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField valueField;
    @FXML private Button addBtn;
    @FXML private Button updateBtn;
    @FXML private Button deleteBtn;
    @FXML private Button clearBtn;
    @FXML private VBox mainContainer;

    private DataManagementService dataService = new DataManagementService();
    private ObservableList<DataEntry> dataEntries = FXCollections.observableArrayList();
    private Stage primaryStage;

    public DataManagementController(TabPane tabPane) {
        tabPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                this.primaryStage = (Stage) newScene.getWindow();
            }
        });
    }

    public VBox getView() {
        if (mainContainer == null) {
            initializeUI();
        }
        return mainContainer;
    }

    private void initializeUI() {
        // Create UI components
        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        dataTableView = new TableView<>();
        dataTableView.getColumns().addAll(idColumn, nameColumn, valueColumn);
        dataTableView.setItems(dataEntries);

        idField = new TextField();
        idField.setPromptText("ID");

        nameField = new TextField();
        nameField.setPromptText("Name");

        valueField = new TextField();
        valueField.setPromptText("Value");

        addBtn = new Button("Add");
        updateBtn = new Button("Update");
        deleteBtn = new Button("Delete");
        clearBtn = new Button("Clear");

        // Layout
        VBox formBox = new VBox(10,
                new Label("ID:"), idField,
                new Label("Name:"), nameField,
                new Label("Value:"), valueField,
                new HBox(10, addBtn, updateBtn, deleteBtn, clearBtn)
        );

        mainContainer = new VBox(15,
                new Label("Data Entries:"), dataTableView,
                formBox
        );
        mainContainer.setPadding(new Insets(15));

        // Event handlers
        setupEventHandlers();
        loadData();
    }

    private void setupEventHandlers() {
        // Table selection listener
        dataTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(String.valueOf(newSelection.getId()));
                nameField.setText(newSelection.getName());
                valueField.setText(newSelection.getValue());
            }
        });

        addBtn.setOnAction(e -> {
            try {
                DataEntry entry = new DataEntry(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        valueField.getText()
                );
                dataService.addEntry(entry);
                loadData();
                clearForm();
                LoggerUtil.logInfo("Entry added successfully");
            } catch (Exception ex) {
                LoggerUtil.logError("Error adding entry", ex);
                showErrorAlert("Add Error", ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> {
            try {
                dataService.updateEntry(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        valueField.getText()
                );
                loadData();
                LoggerUtil.logInfo("Entry updated successfully");
            } catch (Exception ex) {
                LoggerUtil.logError("Error updating entry", ex);
                showErrorAlert("Update Error", ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            try {
                dataService.deleteEntry(Integer.parseInt(idField.getText()));
                loadData();
                clearForm();
                LoggerUtil.logInfo("Entry deleted successfully");
            } catch (Exception ex) {
                LoggerUtil.logError("Error deleting entry", ex);
                showErrorAlert("Delete Error", ex.getMessage());
            }
        });

        clearBtn.setOnAction(e -> clearForm());
    }

    private void loadData() {
        dataEntries.setAll(dataService.getAllEntries());
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        valueField.clear();
        dataTableView.getSelectionModel().clearSelection();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}