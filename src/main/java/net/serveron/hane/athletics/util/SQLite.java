package net.serveron.hane.athletics.util;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLite {
    private final File dataBaseFile;
    public static Connection connection;
    private PreparedStatement stmtForGet;
    private PreparedStatement stmtForSet;
    private PreparedStatement stmtForDelete;
    private final String tableName;

    public SQLite(File pluginDataFolder, String dbname, String tableName){
        File dataBaseFile = new File(pluginDataFolder, dbname+".db");
        this.dataBaseFile = dataBaseFile;
        this.tableName = tableName;

        if (!dataBaseFile.exists()){
            try {
                dataBaseFile.createNewFile();
            } catch (IOException e) {
                System.out.println("File write error: "+dbname+".db");
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
                Class.forName("org.sqlite.JDBC");//ドライバー
                connection = DriverManager.getConnection("jdbc:sqlite:" + dataBaseFile);//データベースに接続

                //  テーブルの作成
                //		key         : varchar
                //		value        : varchar
                //  存在すれば、無視される

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS "+tableName+"( key varchar primary key, value varchar);");
                preparedStatement.executeUpdate();

                stmtForGet = connection.prepareStatement("SELECT * FROM "+tableName+" WHERE uuid = ?;");
                stmtForSet = connection.prepareStatement("REPLACE INTO "+tableName+" (key, value) values (?, ?)");
                stmtForDelete = connection.prepareStatement("DELETE FROM "+tableName+" WHERE uuid = ?;");
            }
            System.out.println("[SQLite] 正常に接続できました。");
            return true;
        } catch(SQLException | ClassNotFoundException e){
            System.out.println("[SQLite] 正常に接続できませんでした。");
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e){
            System.out.println("[SQLite] なぜかすでに接続があります。");
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
                System.out.println("[SQLite] 接続が正常に終了できました。");
            } else {
                System.out.println("[SQLite] すでにSQLiteの接続がありませんでした。");
            }
        } catch (SQLException e){
            System.out.println("[SQLite] SQLiteの接続が正常に終了できなかったようです。");
            e.printStackTrace();
        }
    }

    public String getData(String key){
        try{
            stmtForGet.clearParameters();
            stmtForGet.setString(1,key);
            ResultSet rs = stmtForGet.executeQuery();
            String data = "";
            if ( rs.next() ) {
                data = rs.getString("data");
            }
            rs.close();
            System.out.println("[SQLite] データを読み込みました。");
            return data;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("[SQLite] データの読み込みに失敗しました。");
            return null;
        }
    }

    public boolean setData(String key,String data){
        try{
            stmtForSet.clearParameters();
            stmtForGet.setString(1,key);
            stmtForSet.setString(2,data);
            stmtForSet.executeUpdate();
            System.out.println("[SQLite] データを書き込みました。");
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("[SQLite] データの書き込みに失敗しました。");
            return false;
        }
    }

    public boolean deleteData(String key){
        try{
            stmtForDelete.clearParameters();
            stmtForDelete.setString(1,key);
            stmtForDelete.executeUpdate();
            System.out.println("[SQLite] データを書き込みました。");
            return true;
        } catch (SQLException e){
            System.out.println("[SQLite] データの書き込みに失敗しました。");
            return false;
        }
    }
}
