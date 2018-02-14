package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private final ObservableList<Channel> data =
            FXCollections.observableArrayList(
                    new Channel("S01", "455", "Hírek"),
                    new Channel("S01", "455", "Hírek"),
                    new Channel("S01", "455", "Hírek"),
                    new Channel("S01", "455", "Hírek")
            );

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn channelCol = new TableColumn("Csatorna");
        channelCol.setMinWidth(10);
        channelCol.setCellFactory(TextFieldTableCell.forTableColumn());
        channelCol.setCellValueFactory(new PropertyValueFactory<Channel, String>("channel"));


        TableColumn frekCol = new TableColumn("Frekvencia");
        frekCol.setMinWidth(15);
        frekCol.setCellFactory(TextFieldTableCell.forTableColumn());
        frekCol.setCellValueFactory(new PropertyValueFactory<Channel, String>("frek"));

        TableColumn nameCol = new TableColumn("Csatorna neve");
        nameCol.setMinWidth(100);
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellValueFactory(new PropertyValueFactory<Channel, String>("name"));

        table.getColumns().addAll(channelCol, frekCol, nameCol);
        table.setItems(data);
    }
}
