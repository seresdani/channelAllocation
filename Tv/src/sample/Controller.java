package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;

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

    private final String MENU_CHANNEL = "Csatornák";
    private final String MENU_EXIT = "Kilépés";
    private final String MENU_LIST = "Lista";
    private final String MENU_IMPORT = "Importálás";

    private final ObservableList<Channel> data =
            FXCollections.observableArrayList(
                    new Channel("S01", "455", "Hírek"),
                    new Channel("S01", "455", "Hírek"),
                    new Channel("S01", "455", "Hírek"),
                    new Channel("S01", "455", "Hírek")
            );

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
        menuPane.getChildren().add(treeView);

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                String selectedMenu = selectedItem.getValue();

                if(null != selectedMenu) switch (selectedMenu) {
                    case MENU_CHANNEL:
                        if (selectedItem.isExpanded()) {
                            selectedItem.setExpanded(false);
                        } else selectedItem.setExpanded(true);
                        break;

                    case MENU_EXIT:
                        System.exit(0);
                        break;

                }
            })
        };
    }

    public void setTableData () {

        TableColumn channelCol = new TableColumn("Csatorna");
        channelCol.setMinWidth(150);
        channelCol.setStyle("-fx-alignment: CENTER;");
        channelCol.setCellFactory(TextFieldTableCell.forTableColumn());
        channelCol.setCellValueFactory(new PropertyValueFactory<Channel, String>("channel"));

        channelCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Channel, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Channel, String> event) {
                        ((Channel) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())).setChannel(event.getNewValue());
                    }
                }
        );


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
        checkCol.setCellFactory(CheckBoxTableCell.forTableColumn(channelCol));

        table.getColumns().addAll(channelCol, frekCol, nameCol, checkCol);
        table.setItems(data);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableData();
        setMenuData();

    }
}
