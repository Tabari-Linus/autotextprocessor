package lii.autotexttprocessor.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lii.autotexttprocessor.service.DataManagementService;
import lii.autotexttprocessor.service.FileService;
import lii.autotexttprocessor.model.DataEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import lii.autotexttprocessor.util.RegexUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private ComboBox<String> regexPatternComboBox;
    @FXML private TextField customRegexField;
    @FXML private TextArea regexInputArea;
    @FXML private ListView<String> regexMatchList;
    @FXML private TableView<DataEntry> dataEntryTable;
    @FXML private TableColumn<DataEntry, String> columnId;
    @FXML private TableColumn<DataEntry, String> columnContent;
    @FXML private TableColumn<DataEntry, String> columnActions;

    private final FileService fileService = new FileService();
    private final DataManagementService dataService = new DataManagementService();

    @FXML
    public void initialize() {
        setupRegexTab();
        setupDataEntryTab();
    }

    private void setupRegexTab() {
        regexPatternComboBox.getItems().addAll("Email", "Date", "Time", "Number", "Custom");
        regexPatternComboBox.getSelectionModel().selectFirst();
        regexPatternComboBox.setOnAction(e -> toggleCustomRegexField());
    }

    private void setupDataEntryTab() {
        columnId.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        columnContent.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getContent()));
        columnActions.setCellFactory(getActionCellFactory());

        refreshDataTable();
        setupTableContextMenu();
    }

    private void toggleCustomRegexField() {
        customRegexField.setVisible("Custom".equals(regexPatternComboBox.getValue()));
    }

    @FXML
    private void handleRegexExecute() {
        String pattern = regexPatternComboBox.getValue();
        String inputText = regexInputArea.getText();

        if (inputText.isEmpty()) {
            showAlert("Error", "No Input", "Please enter text to process");
            return;
        }

        String regex = switch (pattern) {
            case "Email" -> "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
            case "Date" -> "\\b\\d{4}-\\d{2}-\\d{2}\\b";
            case "Time" -> "\\b\\d{2}:\\d{2}(:\\d{2})?\\b";
            case "Number" -> "\\b\\d+(\\.\\d+)?\\b";
            case "Custom" -> {
                if (customRegexField.getText().isEmpty()) {
                    showAlert("Error", "Missing Pattern", "Please enter a custom regular expression");
                    yield "";
                }
                yield customRegexField.getText();
            }
            default -> "";
        };

        if (!regex.isEmpty()) {
            List<String> matches = RegexUtil.findMatches(inputText, regex);
            regexMatchList.setItems(FXCollections.observableArrayList(matches));
        }
    }

    @FXML
    private void handleOpenFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                String content = fileService.readFile(file.getAbsolutePath());
                regexInputArea.setText(content);
            } catch (IOException e) {
                showAlert("Error", "File Error", "Could not open file: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSaveFile() {
        if (regexInputArea.getText().isEmpty()) {
            showAlert("Error", "No Content", "There is no text to save");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                fileService.writeFile(file.getAbsolutePath(), regexInputArea.getText());
                showAlert("Success", "File Saved", "Content saved successfully");
            } catch (IOException e) {
                showAlert("Error", "File Error", "Could not save file: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLoadData() {
        refreshDataTable();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAddEntry() {
        DataEntry newEntry = new DataEntry(
                dataService.getNextId(),
                "New Entry",
                "Sample content");
        dataService.addEntry(newEntry);
        refreshDataTable();
    }

    private void refreshDataTable() {
        dataEntryTable.setItems(FXCollections.observableArrayList(dataService.getAllEntries()));
    }

    private void setupTableContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(this::handleDeleteEntry);
        contextMenu.getItems().add(deleteItem);
        dataEntryTable.setContextMenu(contextMenu);
    }

    private void handleDeleteEntry(ActionEvent event) {
        DataEntry selected = dataEntryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dataService.deleteEntry(selected.getId());
            refreshDataTable();
        } else {
            showAlert("Error", "No Selection", "Please select an entry to delete");
        }
    }

    private Callback<TableColumn<DataEntry, String>, TableCell<DataEntry, String>> getActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox actionBox = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setOnAction(e -> handleEditAction());
                deleteBtn.setOnAction(e -> handleDeleteAction());
            }

            private void handleEditAction() {
                DataEntry entry = getTableView().getItems().get(getIndex());
                TextInputDialog dialog = new TextInputDialog(entry.getContent());
                dialog.setTitle("Edit Entry");
                dialog.setHeaderText("Edit entry content:");
                dialog.setContentText("Content:");

                dialog.showAndWait().ifPresent(newContent -> {
                    entry.setContent(newContent);
                    dataService.updateEntry(entry.getId(),entry.getName(), newContent);
                    refreshDataTable();
                });
            }

            private void handleDeleteAction() {
                DataEntry entry = getTableView().getItems().get(getIndex());
                dataService.deleteEntry(entry.getId());
                refreshDataTable();
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        };
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}