package com.github.rypengu23.bossbartrainannounce.model;


public class AnnounceInfoModel {

    private String UUID;
    private int nextOrSoon;
    private String worldName;
    private int posX;
    private int posY;
    private int posZ;
    private String nextStationNameKanji;
    private String lineNameJP;
    private String boundJP;
    private String boundEN;
    private String typeJP;
    private String exit;
    private boolean terminal;
    private int direction;
    private String viaLineNameJP;
    private String viaLineOwnerUUID;
    private boolean redstone;
    private int onOrOff;
    private int redstonePosX;
    private int redstonePosY;
    private int redstonePosZ;
    private boolean announceFastFlag;

    public AnnounceInfoModel(String UUID, int nextOrSoon, String worldName, int posX, int posY, int posZ, String nextStationNameKanji, String lineNameJP, String boundJP, String boundEN, String typeJP, String exit, boolean terminal, int direction, String viaLineNameJP, String viaLineOwnerUUID, boolean redstone, int onOrOff, int redstonePosX, int redstonePosY, int redstonePosZ, boolean announceFastFlag) {
        this.UUID = UUID;
        this.nextOrSoon = nextOrSoon;
        this.worldName = worldName;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.nextStationNameKanji = nextStationNameKanji;
        this.lineNameJP = lineNameJP;
        this.boundJP = boundJP;
        this.boundEN = boundEN;
        this.typeJP = typeJP;
        this.exit = exit;
        this.terminal = terminal;
        this.direction = direction;
        this.viaLineNameJP = viaLineNameJP;
        this.viaLineOwnerUUID = viaLineOwnerUUID;
        this.redstone = redstone;
        this.onOrOff = onOrOff;
        this.redstonePosX = redstonePosX;
        this.redstonePosY = redstonePosY;
        this.redstonePosZ = redstonePosZ;
        this.announceFastFlag = announceFastFlag;
    }

    public AnnounceInfoModel() {

    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getNextOrSoon() {
        return nextOrSoon;
    }

    public void setNextOrSoon(int nextOrSoon) {
        this.nextOrSoon = nextOrSoon;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public void setPosZ(int posZ) {
        this.posZ = posZ;
    }

    public String getNextStationNameKanji() {
        return nextStationNameKanji;
    }

    public void setNextStationNameKanji(String nextStationNameKanji) {
        this.nextStationNameKanji = nextStationNameKanji;
    }

    public String getLineNameJP() {
        return lineNameJP;
    }

    public void setLineNameJP(String lineNameJP) {
        this.lineNameJP = lineNameJP;
    }

    public String getBoundJP() {
        return boundJP;
    }

    public void setBoundJP(String boundJP) {
        this.boundJP = boundJP;
    }

    public String getBoundEN() {
        return boundEN;
    }

    public void setBoundEN(String boundEN) {
        this.boundEN = boundEN;
    }

    public String getTypeJP() {
        return typeJP;
    }

    public void setTypeJP(String typeJP) {
        this.typeJP = typeJP;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getViaLineNameJP() {
        return viaLineNameJP;
    }

    public void setViaLineNameJP(String viaLineNameJP) {
        this.viaLineNameJP = viaLineNameJP;
    }

    public String getViaLineOwnerUUID() {
        return viaLineOwnerUUID;
    }

    public void setViaLineOwnerUUID(String viaLineOwnerUUID) {
        this.viaLineOwnerUUID = viaLineOwnerUUID;
    }

    public boolean isRedstone() {
        return redstone;
    }

    public void setRedstone(boolean redstone) {
        this.redstone = redstone;
    }

    public int getOnOrOff() {
        return onOrOff;
    }

    public void setOnOrOff(int onOrOff) {
        this.onOrOff = onOrOff;
    }

    public int getRedstonePosX() {
        return redstonePosX;
    }

    public void setRedstonePosX(int redstonePosX) {
        this.redstonePosX = redstonePosX;
    }

    public int getRedstonePosY() {
        return redstonePosY;
    }

    public void setRedstonePosY(int redstonePosY) {
        this.redstonePosY = redstonePosY;
    }

    public int getRedstonePosZ() {
        return redstonePosZ;
    }

    public void setRedstonePosZ(int redstonePosZ) {
        this.redstonePosZ = redstonePosZ;
    }

    public boolean isAnnounceFastFlag() {
        return announceFastFlag;
    }

    public void setAnnounceFastFlag(boolean announceFastFlag) {
        this.announceFastFlag = announceFastFlag;
    }
}
