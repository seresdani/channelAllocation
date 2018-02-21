package sample;

import java.sql.*;
import java.util.ArrayList;

public class DB {

    final String URL = "jdbc:derby:DB;create=true";
    final String USERNAME = "";
    final String PASSWORD = "";

    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;

    public DB () {

        //Megpróbáljuk életre kelteni
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("A híd létrejött");
        } catch (SQLException ex) {
            System.out.println("Valami baj van a connection (híd) létrehozásakor.");
            System.out.println(""+ex);
        }

        //Ha életre kelt, csinálunk egy megpakolható teherautót
        if (conn != null){
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Valami baj van van a createStatament (teherautó) létrehozásakor.");
                System.out.println(""+ex);
            }
        }

        //Megnézzük, hogy üres-e az adatbázis? Megnézzük, létezik-e az adott adattábla.
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a DatabaseMetaData (adatbázis leírása) létrehozásakor..");
            System.out.println(""+ex);
        }

        try {
            ResultSet rs = dbmd.getTables(null, "APP", "CHANNELS", null);
            if(!rs.next())
            {
                createStatement.execute("create table channels(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), frek float(5), name varchar(50))");
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adattáblák létrehozásakor.");
            System.out.println(""+ex);
        }
    }

    public ArrayList<Channel> getAllChannels(){
        String sql = "select * from channels";
        ArrayList<Channel> channels = null;
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            channels = new ArrayList<>();

            while (rs.next()){
                Channel actualChannel = new Channel(rs.getString("id"), rs.getString("frek"),rs.getString("name"));
                channels.add(actualChannel);
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van a channelek kiolvasásakor");
            System.out.println(""+ex);
        }
        return channels;
    }

    public void addChannel(Channel channel){
        try {
            String sql = "insert into channels (frek, name) values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, channel.getFrek());
            preparedStatement.setString(2, channel.getName());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a channel hozzáadásakor");
            System.out.println(""+ex);
        }
    }

    public void updateChannel(Channel channel){
        try {
            String sql = "update channels set frek = ?, name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, channel.getFrek());
            preparedStatement.setString(2, channel.getName());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a channel hozzáadásakor");
            System.out.println(""+ex);
        }
    }

    public String getId(Channel channel) {
        String sql = "select * from channels";
        String ids = "";
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            while (rs.next()){
                if(channel.getName().equals(rs.getString("name"))) {
                    ids =  rs.getString("id");
                    System.out.println(ids);
                }
            }
        } catch (SQLException e) {
            System.out.println("Valami baj van az ID lekérdezésekor");
            System.out.println(""+e);
        }
        return ids;
    }

    public void sendSQL(String sql){
        try {
            createStatement.execute(sql);
        }catch (Exception e) {
            System.out.println("Valami baj van az SQL paranccsal");
            System.out.println(""+e);
        }
    }

}
