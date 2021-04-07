package net.serveron.hane.athletics.util;

import net.serveron.hane.athletics.Athletics;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLite {
    private final File dataFolder;
    public static Connection connection;
    private PreparedStatement stmtForGet;
    private PreparedStatement stmtForSet;
    private PreparedStatement stmtForDelete;
    private PreparedStatement stmtForGetAt;
    private PreparedStatement stmtForSetAt;

    public SQLite(Athletics plugin, String dbname){

        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        this.dataFolder = dataFolder;
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().info("File write error: "+dbname+".db");
            }
        }
    }

    public boolean openConnection(){
        try{
            if (connection != null && !connection.isClosed()) {
                throw new IllegalStateException();
            }

            synchronized ( this ) {
                if ( connection != null && !connection.isClosed() ) {
                    throw new IllegalStateException();
                }
                Class.forName("org.sqlite.JDBC");//1 ドライバー
                connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);//データベースに接続


                //  テーブルの作成
                //		uuid        : varchar(36)	player uuid
                //		name        : varchar(20)	player name
                //		athle1_num  : boolean       player
                //		athle2_time : TIMESTAMP
                //		athle3_time : TIMESTAMP
                //		athle4_time : TIMESTAMP
                //		pvp_win     : int
                //		pvp_lose    : int
                //		fishing     : int
                //  存在すれば、無視される

                PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player( uuid varchar(36) primary key, name varchar(20), athle1 int, " +
                        "athle2 int,  athle3 int, athle4 int, pvp_win int, pvp_lose int, fishing int);");
                preparedStatement.executeUpdate();
                PreparedStatement preparedStatement2 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS athletics( athletic varchar(10) primary key, player_name varchar(20) ,time int, num int);");
                preparedStatement2.executeUpdate();


                stmtForGet = connection.prepareStatement("SELECT * FROM player WHERE uuid = ?;");
                stmtForSet = connection.prepareStatement("REPLACE INTO player (uuid, name, athle1, athle2, athle3, athle4, pvp_win, pvp_lose, fishing) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                stmtForDelete = connection.prepareStatement("DELETE FROM player WHERE uuid = ?;");

                stmtForGetAt = connection.prepareStatement("SELECT * FROM athletics;");
                stmtForSetAt = connection.prepareStatement("REPLACE INTO athletics (athletic, player_name, time, num) values (?, ?, ?, ?)");

            }
            System.out.println("[HaneServerLobby] SQLiteに正常に接続できました。");
            return true;
        } catch(SQLException | ClassNotFoundException e){
            System.out.println("[HaneServerLobby] SQLiteに正常に接続できませんでした。");
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e){
            System.out.println("[HaneServerLobby] なぜかすでに接続があります。");
            return false;
        }

    }

    public void closeDisconnect(){
        try{
            if (connection != null && !connection.isClosed()) {
                connection.close();
                stmtForGet.close();
                stmtForSet.close();
                stmtForDelete.close();
                stmtForGetAt.close();
                stmtForSetAt.close();
                //stmtForDeleteAt.close();
                System.out.println("[HaneServerLobby] SQLiteの接続が正常に終了できました。");
            } else {
                System.out.println("[HaneServerLobby] すでにMySQLの接続がありませんでした。");
            }
        } catch (SQLException e){
            System.out.println("[HaneServerLobby] SQLiteの接続が正常に終了できなかったようです。");
            e.printStackTrace();
        }
    }

    public PlayerData getPlayerData( String uuid ){

        try{
            stmtForGet.clearParameters();
            stmtForGet.setString(1,uuid);
            ResultSet rs = stmtForGet.executeQuery();

            PlayerData playerData = null;
            if ( rs.next() ) {
                //athle1_num int, athle2_time TIMESTAMP,  athle3_time TIMESTAMP, athle4_time TIMESTAMP, pvp_win int, pvp_lose int)
                String name = rs.getString( "name" );
                int At1 = rs.getInt( "athle1" );
                int At2 = rs.getInt( "athle2" );
                int At3 = rs.getInt( "athle3" );
                int At4 = rs.getInt( "athle4" );
                int pvp_win = rs.getInt( "pvp_win" );
                int pvp_lose = rs.getInt( "pvp_lose" );
                int fishing = rs.getInt( "fishing" );

                playerData = new PlayerData(uuid, name, At1,At2,At3,At4,pvp_win,pvp_lose,fishing);

            }
            rs.close();
            System.out.println("[HaneServerLobby] "+uuid+"のデータを読み込みました。");
            return playerData;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("[HaneServerLobby] データの読み込みに失敗しました。");
            return null;
        }
    }

    public boolean setPlayerData(PlayerData playerData){
        //System.out.println("kokodayo"+playerData.getAt3());
        try{
            stmtForSet.clearParameters();
            stmtForSet.setString(1,playerData.getUuid());
            stmtForSet.setString(2,playerData.getName());
            stmtForSet.setInt(3,playerData.getAt1());
            stmtForSet.setInt(4,playerData.getAt2());
            stmtForSet.setInt(5,playerData.getAt3());
            stmtForSet.setInt(6,playerData.getAt4());
            stmtForSet.setInt(7,playerData.getPvp_win());
            stmtForSet.setInt(8,playerData.getPvp_lose());
            stmtForSet.setInt(9,playerData.getFishing());

            stmtForSet.executeUpdate();
            System.out.println("[HaneServerLobby] "+playerData.getName()+"のデータを書き込みました。");
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("[HaneServerLobby] "+playerData.getName()+"のデータの書き込みに失敗しました。");
            return false;
        }
    }

    public boolean deletePlayerData( String uuid ){
        try{
            stmtForDelete.clearParameters();
            stmtForDelete.setString(1,uuid);
            stmtForDelete.executeUpdate();
            System.out.println("[HaneServerLobby] "+uuid+"のデータを書き込みました。");
            return true;
        } catch (SQLException e){
            System.out.println("[HaneServerLobby] "+uuid+"のデータの書き込みに失敗しました。");
            return false;
        }
    }

    public List<AthleticData> getAthleticData(){

        try{
            ResultSet rs = stmtForGetAt.executeQuery();

            List<AthleticData> athleticsData = new ArrayList<>();
            while ( rs.next() ) {
                athleticsData.add(new AthleticData(rs.getString( "athletic" ), rs.getString("player_name"),
                        rs.getInt( "time" ),rs.getInt( "num" )));

            }

            rs.close();
            System.out.println("[HaneServerLobby] アスレチックの"+rs.getRow()+"件のデータを読み込みました。");
            return athleticsData;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("[HaneServerLobby] アスレチックデータの読み込みに失敗しました。");
            return null;
        }
    }

    public boolean setAthleticData(AthleticData athleticData){
        try{
            stmtForSetAt.clearParameters();
            stmtForSet.setString(1,athleticData.getSQLName());
            stmtForSet.setString(2,athleticData.getPlayerName());
            stmtForSet.setInt(3,athleticData.getTime());
            stmtForSet.setInt(4,athleticData.getNum());

            stmtForSet.executeUpdate();
            System.out.println("[HaneServerLobby] "+athleticData.getSQLName()+"のデータを書き込みました。");
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("[HaneServerLobby] "+athleticData.getSQLName()+"のデータの書き込みに失敗しました。");
            return false;
        }
    }

}
