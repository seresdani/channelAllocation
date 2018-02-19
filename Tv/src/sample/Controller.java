package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.controlsfx.control.textfield.TextFields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Controller implements Initializable{

    @FXML
    TableView table;
    @FXML
    TextField inputChannel;
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
                        System.exit(0);
                        break;

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

        TableColumn frekCol = new TableColumn("Frekvencia");
        frekCol.setMinWidth(200);
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
        nameCol.setMinWidth(250);
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
        checkCol.setMinWidth(74);
        checkCol.setStyle("-fx-alignment: Center;");
        checkCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkCol));

        table.getColumns().addAll(frekCol, nameCol, checkCol);
        table.setItems(data);

    }

    public void addNewChannel (ActionEvent event) {
        if( !inputName.getText().equals("") && !inputFrek.getText().equals("") && !inputChannel.getText().equals("") ){
            data.add(new Channel(inputFrek.getText(), inputName.getText()));
            inputChannel.clear();
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

                    System.out.println(line);

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

                        data.add(new Channel(lines[i].substring(fEnd + 1, sEnd), lines[i].substring(sEnd + 1, cEnd - 1)));

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

        txtField.setPromptText("Filter");
        txtField.textProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable o) {

                if(txtField.textProperty().get().isEmpty()) {
                    table.setItems(data);
                    return;
                }

                ObservableList<Channel> tableItems = FXCollections.observableArrayList();
                ObservableList<TableColumn<Channel, ?>> cols = table.getColumns();

                for(int i=0; i<data.size(); i++) {

                    for(int j=0; j<cols.size(); j++) {

                        TableColumn col = cols.get(j);
                        String cellValue = col.getCellData(data.get(i)).toString();
                        cellValue = cellValue.toLowerCase();

                        if(cellValue.contains(txtField.textProperty().get().toLowerCase())) {

                            tableItems.add(data.get(i));
                            break;

                        }
                    }
                }

                table.setItems(tableItems);

            }

        });

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableData();
        setMenuData();
        //initFilter();

    }
}
