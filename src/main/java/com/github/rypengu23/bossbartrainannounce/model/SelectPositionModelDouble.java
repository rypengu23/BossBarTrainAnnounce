package com.github.rypengu23.bossbartrainannounce.model;

public class SelectPositionModelDouble {

    private String worldName;
    private Double pos1X;
    private Double pos1Y;
    private Double pos1Z;
    private Double pos2X;
    private Double pos2Y;
    private Double pos2Z;
    private boolean selectPos1;
    private boolean selectPos2;

    public SelectPositionModelDouble(String worldName, Double pos1X, Double pos1Y, Double pos1Z, Double pos2X, Double pos2Y, Double pos2Z) {
        this.worldName = worldName;
        this.pos1X = pos1X;
        this.pos1Y = pos1Y;
        this.pos1Z = pos1Z;
        this.pos2X = pos2X;
        this.pos2Y = pos2Y;
        this.pos2Z = pos2Z;
        this.selectPos1 = true;
        this.selectPos2 = true;
    }

    public SelectPositionModelDouble() {
        this.worldName = "";
        this.pos1X = 0.0;
        this.pos1Y = 0.0;
        this.pos1Z = 0.0;
        this.pos2X = 0.0;
        this.pos2Y = 0.0;
        this.pos2Z = 0.0;
        this.selectPos1 = false;
        this.selectPos2 = false;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Double getPos1X() {
        return pos1X;
    }

    public void setPos1X(Double pos1X) {
        this.pos1X = pos1X;
    }

    public Double getPos1Y() {
        return pos1Y;
    }

    public void setPos1Y(Double pos1Y) {
        this.pos1Y = pos1Y;
    }

    public Double getPos1Z() {
        return pos1Z;
    }

    public void setPos1Z(Double pos1Z) {
        this.pos1Z = pos1Z;
    }

    public Double getPos2X() {
        return pos2X;
    }

    public void setPos2X(Double pos2X) {
        this.pos2X = pos2X;
    }

    public Double getPos2Y() {
        return pos2Y;
    }

    public void setPos2Y(Double pos2Y) {
        this.pos2Y = pos2Y;
    }

    public Double getPos2Z() {
        return pos2Z;
    }

    public void setPos2Z(Double pos2Z) {
        this.pos2Z = pos2Z;
    }

    public boolean isSelectPos1() {
        return selectPos1;
    }

    public void setSelectPos1(boolean selectPos1) {
        this.selectPos1 = selectPos1;
    }

    public boolean isSelectPos2() {
        return selectPos2;
    }

    public void setSelectPos2(boolean selectPos2) {
        this.selectPos2 = selectPos2;
    }
}
