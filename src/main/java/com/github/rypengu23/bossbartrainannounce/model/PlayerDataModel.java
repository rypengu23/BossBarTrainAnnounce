package com.github.rypengu23.bossbartrainannounce.model;

public class PlayerDataModel {

    private boolean speedUpFlag = false;

    private boolean showBossBar = true;

    private boolean showChatAnnounce = true;

    public PlayerDataModel() {
    }

    public PlayerDataModel(boolean speedUpFlag, boolean showBossBar, boolean showChatAnnounce) {
        this.speedUpFlag = speedUpFlag;
        this.showBossBar = showBossBar;
        this.showChatAnnounce = showChatAnnounce;
    }

    public boolean isSpeedUpFlag() {
        return speedUpFlag;
    }

    public void setSpeedUpFlag(boolean speedUpFlag) {
        this.speedUpFlag = speedUpFlag;
    }

    public boolean isShowBossBar() {
        return showBossBar;
    }

    public void setShowBossBar(boolean showBossBar) {
        this.showBossBar = showBossBar;
    }

    public boolean isShowChatAnnounce() {
        return showChatAnnounce;
    }

    public void setShowChatAnnounce(boolean showChatAnnounce) {
        this.showChatAnnounce = showChatAnnounce;
    }
}
