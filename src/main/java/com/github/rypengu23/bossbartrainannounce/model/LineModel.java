package com.github.rypengu23.bossbartrainannounce.model;

import java.util.HashMap;

public class LineModel {

    private String UUID;
    private String lineNameJP;
    private String lineNameEN;
    private String lineColor;
    private HashMap<String,String> type;
    private boolean loop;

    public LineModel(){}

    public LineModel(String UUID, String lineNameJP, String lineNameEN, String lineColor, HashMap<String, String> type, boolean loop) {
        this.UUID = UUID;
        this.lineNameJP = lineNameJP;
        this.lineNameEN = lineNameEN;
        this.lineColor = lineColor;
        this.type = type;
        this.loop = loop;
    }

    /**
     * カンマで分割された種別名をHashMapに変換
     * @param typeJP
     * @param typeEN
     * @return
     */
    public HashMap<String,String> convertTypeHashMap(String typeJP, String typeEN){

        String[] typeJPList = typeJP.split(",");
        String[] typeENList = typeEN.split(",");

        if(typeJPList.length != typeENList.length){
            return null;
        }

        HashMap<String,String> resultList = new HashMap<>();
        for(int i=0; i<typeJPList.length; i++){
            resultList.put(typeJPList[i],typeENList[i]);
        }

        return resultList;
    }

    /**
     * 種別のHashMapをカンマ区切りに連結
     * @param type
     * @return
     */
    public String convertTypeCommaStr(int JPorEN, HashMap<String,String> type){

        String result = "";

        boolean first = true;
        if(JPorEN == 0) {
            for (String key : type.keySet()) {

                if (first) {
                    result = key;
                    first = false;
                } else {
                    result = result + "," + key;
                }
            }
        }else{
            for (String value : type.values()) {

                if (first) {
                    result = value;
                    first = false;
                } else {
                    result = result + "," + value;
                }
            }
        }
        return result;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getLineNameJP() {
        return lineNameJP;
    }

    public void setLineNameJP(String lineNameJP) {
        this.lineNameJP = lineNameJP;
    }

    public String getLineNameEN() {
        return lineNameEN;
    }

    public void setLineNameEN(String lineNameEN) {
        this.lineNameEN = lineNameEN;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public HashMap<String, String> getType() {
        return type;
    }

    public void setType(HashMap<String, String> type) {
        this.type = type;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}
