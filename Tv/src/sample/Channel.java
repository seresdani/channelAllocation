package sample;

import javafx.beans.property.SimpleStringProperty;

public class Channel {

    private final SimpleStringProperty id;
    private final SimpleStringProperty frek;
    private final SimpleStringProperty name;

    public Channel() {
        this.id = new SimpleStringProperty("");
        this.frek = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
    }

    public Channel(String ffrek, String fname) {
        this.id = new SimpleStringProperty("");
        this.frek = new SimpleStringProperty(ffrek);
        this.name = new SimpleStringProperty(fname);
    }

    public Channel(String fid, String ffrek, String fname) {
        this.id = new SimpleStringProperty(fid);
        this.frek = new SimpleStringProperty(ffrek);
        this.name = new SimpleStringProperty(fname);
    }

    public String getId() { return id.get(); }

    public String getFrek() {
        return frek.get();
    }

    public String getName() {
        return name.get();
    }

    public void setId(String Id) {
        this.id.set(Id);
    }

    public void setFrek(String frek) {
        this.frek.set(frek);
    }

    public void setName(String name) {
        this.name.set(name);
    }

}
