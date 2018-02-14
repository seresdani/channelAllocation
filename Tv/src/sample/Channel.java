package sample;

import javafx.beans.property.SimpleStringProperty;

public class Channel {

    private final SimpleStringProperty channel;
    private final SimpleStringProperty frek;
    private final SimpleStringProperty name;

    public Channel() {
        this.channel = new SimpleStringProperty("");
        this.frek = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
    }

    public Channel(String fchannel, String ffrek, String fname) {
        this.channel = new SimpleStringProperty(fchannel);
        this.frek = new SimpleStringProperty(ffrek);
        this.name = new SimpleStringProperty(fname);
    }

    public String getChannel() {
        return channel.get();
    }

    public String getFrek() {
        return frek.get();
    }

    public String getName() {
        return name.get();
    }

    public void setChannel(String channel) {
        this.channel.set(channel);
    }

    public void setFrek(String frek) {
        this.frek.set(frek);
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
