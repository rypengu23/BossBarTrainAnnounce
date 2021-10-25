package com.github.rypengu23.bossbartrainannounce.model;

public class SelectPositionModel {

    private String worldName;
    private int pos1X;
    private int pos1Y;
    private int pos1Z;
    private int pos2X;
    private int pos2Y;
    private int pos2Z;
    private boolean selectPos1;
    private boolean selectPos2;

    public SelectPositionModel(String worldName, int pos1X, int pos1Y, int pos1Z, int pos2X, int pos2Y, int pos2Z) {
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

    public SelectPositionModel() {
        this.worldName = "";
        this.pos1X = 0;
        this.pos1Y = 0;
        this.pos1Z = 0;
        this.pos2X = 0;
        this.pos2Y = 0;
        this.pos2Z = 0;
        this.selectPos1 = false;
        this.selectPos2 = false;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public int getPos1X() {
        return pos1X;
    }

    public void setPos1X(int pos1X) {
        this.pos1X = pos1X;
    }

    public int getPos1Y() {
        return pos1Y;
    }

    public void setPos1Y(int pos1Y) {
        this.pos1Y = pos1Y;
    }

    public int getPos1Z() {
        return pos1Z;
    }

    public void setPos1Z(int pos1Z) {
        this.pos1Z = pos1Z;
    }

    public int getPos2X() {
        return pos2X;
    }

    public void setPos2X(int pos2X) {
        this.pos2X = pos2X;
    }

    public int getPos2Y() {
        return pos2Y;
    }

    public void setPos2Y(int pos2Y) {
        this.pos2Y = pos2Y;
    }

    public int getPos2Z() {
        return pos2Z;
    }

    public void setPos2Z(int pos2Z) {
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
