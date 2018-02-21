package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Controller implements Initializable{

    @FXML
    TableView table;
    @FXML
    TextField inputFrek;
    @FXML
    TextField inputName;
    @FXML
    Button addNewChannel;
    @FXML
    StackPane menuPane;
    @FXML
    Pane channelPane;
    @FXML
    Pane importPane;
    @FXML
    TextField path;
    @FXML
    Button importButton;
    @FXML
    Button browserButton;
    @FXML
    TextField txtField;
    @FXML
    AnchorPane mainPane;

    DB db = new DB();

    private final String MENU_CHANNEL = "Csatornák";
    private final String MENU_EXIT = "Kilépés";
    private final String MENU_LIST = "Lista";
    private final String MENU_IMPORT = "Importálás";

    private final ObservableList<Channel> data = FXCollections.observableArrayList();

    public void setMenuData() {

        TreeItem<String> treeItemRoot1 = new TreeItem<>("Menü");
        TreeView<String> treeView = new TreeView<>(treeItemRoot1);
        treeView.setShowRoot(false);

        TreeItem<String> nodeItemA = new TreeItem<>(MENU_CHANNEL);
        TreeItem<String> nodeItemB = new TreeItem<>(MENU_EXIT);

        TreeItem<String> nodeItemA1 = new TreeItem<>(MENU_LIST);
        TreeItem<String> nodeItemA2 = new TreeItem<>(MENU_IMPORT);

        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
        treeItemRoot1.getChildren().addAll(nodeItemA, nodeItemB);

        nodeItemA.setExpanded(true);
        menuPane.getChildren().add(treeView);

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                String selectedMenu = selectedItem.getValue();

                if(null != selectedMenu) switch (selectedMenu) {
                    case MENU_CHANNEL:
                        if (selectedItem.isExpanded())
                            {
                            selectedItem.setExpanded(false);
                            break;
                            }
                        else
                            {
                            selectedItem.setExpanded(true);
                            break;
                            }

                    case MENU_EXIT:
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            System.exit(0);
                            break;
                        } catch (Exception e) {
                            System.out.println("Valami baj van a kilépéssel");
                            System.out.println(""+e);
                        }

                    case MENU_LIST:
                        importPane.setVisible(false);
                        channelPane.setVisible(true);
                        break;

                    case MENU_IMPORT:
                        channelPane.setVisible(false);
                        importPane.setVisible(true);
                        break;

                }
            }
        });
    }

    public void setTableData () {

        TableColumn idCol = new TableColumn("Sorszám");
        idCol.setMinWidth(50);
        idCol.setStyle("-fx-alignment: CENTER");
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setCellValueFactory(new PropertyValueFactory<Channel, String>("id"));

        TableColumn frekCol = new TableColumn("Frekvencia");
        frekCol.setMinWidth(150);
        frekCol.setStyle("-fx-alignment: CENTER;");
        frekCol.setCellFactory(TextFieldTableCell.forTableColumn());
        frekCol.setCellValueFactory(new PropertyValueFactory<Channel, String>("frek"));

        frekCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Channel, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Channel, String> event) {
                        ((Channel) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())).setFrek(event.getNewValue());
                    }
                }
        );

        TableColumn nameCol = new TableColumn("Csatorna neve");
        nameCol.setMinWidth(350);
        nameCol.setStyle("-fx-alignment: CENTER;");
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellValueFactory(new PropertyValueFactory<Channel, String>("name"));

        nameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Channel, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Channel, String> event) {
                        ((Channel) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())).setName(event.getNewValue());
                    }
                }
        );

        TableColumn checkCol = new TableColumn("Beállítva");
        checkCol.setMinWidth(110);
        checkCol.setStyle("-fx-alignment: Center;");
        checkCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkCol));

        table.getColumns().addAll(idCol, frekCol, nameCol, checkCol);

        data.addAll(db.getAllChannels());

        table.setItems(data);

    }

    public void addNewChannel (ActionEvent event) throws SQLException {
        if( !inputName.getText().equals("") && !inputFrek.getText().equals("") ){
            Channel newChannel = new Channel(inputFrek.getText(), inputName.getText());
            db.addChannel(newChannel);
            newChannel.setId(db.getId(newChannel));
            data.add(newChannel);
            inputFrek.clear();
            inputName.clear();
        }
    }

    public void setImportPane (ActionEvent event) {

        final FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Szöveges fájl (TXT)", "*.txt")
        );

        File file = fileChooser.showOpenDialog(importPane.getScene().getWindow());

        path.setText(file.getAbsolutePath());

    }

    public void setFilePath (ActionEvent event) {

        try (BufferedReader br = new BufferedReader(new FileReader(path.getText()))) {

            String line;
            while ((line = br.readLine()) != null) {
                if(Pattern.matches("^.* ...,.. .*$",line)){

                    String[] lines = line.split("\\u2028");

                    for(int i=0; i<lines.length;i++){

                        int fEnd = lines[i].indexOf(" ", 5);
                        int sEnd = lines[i].indexOf(" ", 12);

                        int cEnd;
                        if(lines[i].contains("magyar")){
                            cEnd = lines[i].lastIndexOf("magyar");
                        }
                        else {
                            cEnd = lines[i].lastIndexOf(" ") + 1;
                        }
                        Channel newChannel = new Channel(lines[i].substring(fEnd + 1, sEnd).replace(',', '.'), lines[i].substring(sEnd + 1, cEnd - 1));
                        db.addChannel(newChannel);
                        newChannel.setId(db.getId(newChannel));
                        data.add(newChannel);

                    }

                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

        importPane.setVisible(false);
        channelPane.setVisible(true);

    }

    private void initFilter() {

        txtField.setPromptText("Keresés...");

        FilteredList<Channel> filteredData = new FilteredList<>(data, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txtField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(channel -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name field in your object with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(channel.getFrek()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    // Filter matches first name.

                } else if (String.valueOf(channel.getName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }

                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Channel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mainPane.setLayoutX(5);
        mainPane.setLayoutY(5);

        setTableData();
        setMenuData();
        initFilter();

    }
}
