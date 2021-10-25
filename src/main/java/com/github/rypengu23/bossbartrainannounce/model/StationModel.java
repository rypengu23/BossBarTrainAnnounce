package com.github.rypengu23.bossbartrainannounce.model;

public class StationModel {

    private String UUID;
    private String lineNameJp;
    private String stationNameKanji;
    private String stationNameEnglish;
    private String stationNameKatakana;
    private SelectPositionModel selectPositionModel;
    private String number;

    public StationModel(String UUID, String lineNameJp, String stationNameKanji, String stationNameEnglish, String stationNameKatakana, SelectPositionModel selectPositionModel, String number) {
        this.UUID = UUID;
        this.lineNameJp = lineNameJp;
        this.stationNameKanji = stationNameKanji;
        this.stationNameEnglish = stationNameEnglish;
        this.stationNameKatakana = stationNameKatakana;
        this.selectPositionModel = selectPositionModel;
        this.number = number;
    }

    public StationModel() {
    }

    public String getStationNameKanji() {
        return stationNameKanji;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getLineNameJp() {
        return lineNameJp;
    }

    public void setLineNameJp(String lineNameJp) {
        this.lineNameJp = lineNameJp;
    }

    public void setStationNameKanji(String stationNameKanji) {
        this.stationNameKanji = stationNameKanji;
    }

    public String getStationNameEnglish() {
        return stationNameEnglish;
    }

    public void setStationNameEnglish(String stationNameEnglish) {
        this.stationNameEnglish = stationNameEnglish;
    }

    public String getStationNameKatakana() {
        return stationNameKatakana;
    }

    public void setStationNameKatakana(String stationNameKatakana) {
        this.stationNameKatakana = stationNameKatakana;
    }

    public SelectPositionModel getSelectPositionModel() {
        return selectPositionModel;
    }

    public void setSelectPositionModel(SelectPositionModel selectPositionModel) {
        this.selectPositionModel = selectPositionModel;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
