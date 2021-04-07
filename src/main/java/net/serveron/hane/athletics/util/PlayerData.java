package net.serveron.hane.athletics.util;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    private static class Data{
        private final String key;
        private final String value;
        public Data(String key,String value){
            this.key = key;
            this.value = value;
        }
        public String getKey() { return key; }
        public String getValue() { return value; }
    }

    private final String uuid;
    private List<Data> dataList = new ArrayList<>();

    public PlayerData(String uuid, String dataJson){
        this.uuid = uuid;
        dataList = stringToList(dataJson);
    }

    private List<Data> stringToList(String str){
        List<Data> dList = new ArrayList<>();
        String[] result = str.split(",");
        for(String res : result){
            String[] key_value = str.split(":");
            if(key_value.length==2){
                dList.add(new Data(key_value[0],key_value[1]));
            }
        }
        return dList;
    }

    public String getUuid() {
        return uuid;
    }

    public String getValue(String key){
        for(Data data : dataList){
            if(data.getKey().equals(key)){
                return data.getValue();
            }
            break;
        }
        return null;
    }

    public String getStringData(){
        StringBuilder res = new StringBuilder();
        for(Data data : dataList){
            res.append(data.getKey()).append(":").append(data.getValue()).append(",");
        }
        res.setLength(res.length()-1);
        return res.toString();
    }
}
