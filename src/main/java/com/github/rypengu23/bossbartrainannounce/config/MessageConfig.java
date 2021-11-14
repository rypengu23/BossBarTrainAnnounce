package com.github.rypengu23.bossbartrainannounce.config;

import org.bukkit.configuration.Configuration;

public class MessageConfig {
    //バージョン
    private final Double version;

    //削除ワード
    private final String removeWord1;
    private final String removeWord2;

    //アナウンス情報
    private final String trainInformationJPAnnounce;
    private final String trainInformationENAnnounce;
    private final String viaTrainInformationJPAnnounce;
    private final String viaTrainInformationENAnnounce;
    private final String loopTrainInformationJPAnnounce;
    private final String loopTrainInformationENAnnounce;
    private final String viaLoopTrainInformationJPAnnounce;
    private final String viaLoopTrainInformationENAnnounce;
    private final String nextStationJPAnnounce;
    private final String nextStationENAnnounce;
    private final String nextStationTerminalJPAnnounce;
    private final String nextStationTerminalENAnnounce;
    private final String nextStationJPAnnounceExit;
    private final String nextStationENAnnounceExit;
    private final String nextStationTerminalJPAnnounceExit;
    private final String nextStationTerminalENAnnounceExit;
    private final String soonStationJPAnnounce;
    private final String soonStationENAnnounce;
    private final String soonStationTerminalJPAnnounce;
    private final String soonStationTerminalENAnnounce;
    private final String transferJPAnnounceAnnounce;
    private final String transferENAnnounceAnnounce;
    private final String doorRightSideJPAnnounce;
    private final String doorRightSideENAnnounce;
    private final String doorLeftSideJPAnnounce;
    private final String doorLeftSideENAnnounce;
    //BossBar情報
    private final String nextKanji;
    private final String nextEN;
    private final String nextKatakana;
    private final String soonKanji;
    private final String soonEN;
    private final String stoppingStationKanji;
    private final String stoppingStationEN;

    MessageConfig(Configuration config){
        version = config.getDouble("version");

        removeWord1 = config.getString("removeWord1");
        removeWord2 = config.getString("removeWord2");

        trainInformationJPAnnounce = config.getString("chatAnnounce.trainInformationJPAnnounce");
        trainInformationENAnnounce = config.getString("chatAnnounce.trainInformationENAnnounce");
        viaTrainInformationJPAnnounce = config.getString("chatAnnounce.viaTrainInformationJPAnnounce");
        viaTrainInformationENAnnounce = config.getString("chatAnnounce.viaTrainInformationENAnnounce");
        loopTrainInformationJPAnnounce = config.getString("chatAnnounce.loopTrainInformationJPAnnounce");
        loopTrainInformationENAnnounce = config.getString("chatAnnounce.loopTrainInformationENAnnounce");
        viaLoopTrainInformationJPAnnounce = config.getString("chatAnnounce.viaLoopTrainInformationJPAnnounce");
        viaLoopTrainInformationENAnnounce = config.getString("chatAnnounce.viaLoopTrainInformationENAnnounce");
        nextStationJPAnnounce = config.getString("chatAnnounce.nextStationJPAnnounce");
        nextStationENAnnounce = config.getString("chatAnnounce.nextStationENAnnounce");
        nextStationTerminalJPAnnounce = config.getString("chatAnnounce.nextStationTerminalJPAnnounce");
        nextStationTerminalENAnnounce = config.getString("chatAnnounce.nextStationTerminalENAnnounce");
        nextStationJPAnnounceExit = config.getString("chatAnnounce.nextStationJPAnnounceExit");
        nextStationENAnnounceExit = config.getString("chatAnnounce.nextStationENAnnounceExit");
        nextStationTerminalJPAnnounceExit = config.getString("chatAnnounce.nextStationTerminalJPAnnounceExit");
        nextStationTerminalENAnnounceExit = config.getString("chatAnnounce.nextStationTerminalENAnnounceExit");
        soonStationJPAnnounce = config.getString("chatAnnounce.soonStationJPAnnounce");
        soonStationENAnnounce = config.getString("chatAnnounce.soonStationENAnnounce");
        soonStationTerminalJPAnnounce = config.getString("chatAnnounce.soonStationTerminalJPAnnounce");
        soonStationTerminalENAnnounce = config.getString("chatAnnounce.soonStationTerminalENAnnounce");
        transferJPAnnounceAnnounce = config.getString("chatAnnounce.transferJPAnnounce");
        transferENAnnounceAnnounce = config.getString("chatAnnounce.transferENAnnounce");
        doorRightSideJPAnnounce = config.getString("chatAnnounce.doorRightSideJPAnnounce");
        doorRightSideENAnnounce = config.getString("chatAnnounce.doorRightSideENAnnounce");
        doorLeftSideJPAnnounce = config.getString("chatAnnounce.doorLeftSideJPAnnounce");
        doorLeftSideENAnnounce = config.getString("chatAnnounce.doorLeftSideENAnnounce");

        nextKanji = config.getString("bossBar.nextKanji");
        nextEN = config.getString("bossBar.nextEN");
        nextKatakana = config.getString("bossBar.nextKatakana");
        soonKanji = config.getString("bossBar.soonKanji");
        soonEN = config.getString("bossBar.soonEN");
        stoppingStationKanji = config.getString("bossBar.stoppingStationKanji");
        stoppingStationEN = config.getString("bossBar.stoppingStationEN");
    }

    public Double getVersion() {
        return version;
    }

    public String getRemoveWord1() {
        return removeWord1;
    }

    public String getRemoveWord2() {
        return removeWord2;
    }

    public String getTrainInformationJPAnnounce() {
        return trainInformationJPAnnounce;
    }

    public String getTrainInformationENAnnounce() {
        return trainInformationENAnnounce;
    }

    public String getViaTrainInformationJPAnnounce() {
        return viaTrainInformationJPAnnounce;
    }

    public String getViaTrainInformationENAnnounce() {
        return viaTrainInformationENAnnounce;
    }

    public String getLoopTrainInformationJPAnnounce() {
        return loopTrainInformationJPAnnounce;
    }

    public String getLoopTrainInformationENAnnounce() {
        return loopTrainInformationENAnnounce;
    }

    public String getViaLoopTrainInformationJPAnnounce() {
        return viaLoopTrainInformationJPAnnounce;
    }

    public String getViaLoopTrainInformationENAnnounce() {
        return viaLoopTrainInformationENAnnounce;
    }

    public String getNextStationJPAnnounce() {
        return nextStationJPAnnounce;
    }

    public String getNextStationENAnnounce() {
        return nextStationENAnnounce;
    }

    public String getNextStationTerminalJPAnnounce() {
        return nextStationTerminalJPAnnounce;
    }

    public String getNextStationTerminalENAnnounce() {
        return nextStationTerminalENAnnounce;
    }

    public String getNextStationJPAnnounceExit() {
        return nextStationJPAnnounceExit;
    }

    public String getNextStationENAnnounceExit() {
        return nextStationENAnnounceExit;
    }

    public String getNextStationTerminalJPAnnounceExit() {
        return nextStationTerminalJPAnnounceExit;
    }

    public String getNextStationTerminalENAnnounceExit() {
        return nextStationTerminalENAnnounceExit;
    }

    public String getSoonStationJPAnnounce() {
        return soonStationJPAnnounce;
    }

    public String getSoonStationENAnnounce() {
        return soonStationENAnnounce;
    }

    public String getSoonStationTerminalJPAnnounce() {
        return soonStationTerminalJPAnnounce;
    }

    public String getSoonStationTerminalENAnnounce() {
        return soonStationTerminalENAnnounce;
    }

    public String getTransferJPAnnounceAnnounce() {
        return transferJPAnnounceAnnounce;
    }

    public String getTransferENAnnounceAnnounce() {
        return transferENAnnounceAnnounce;
    }

    public String getDoorRightSideJPAnnounce() {
        return doorRightSideJPAnnounce;
    }

    public String getDoorRightSideENAnnounce() {
        return doorRightSideENAnnounce;
    }

    public String getDoorLeftSideJPAnnounce() {
        return doorLeftSideJPAnnounce;
    }

    public String getDoorLeftSideENAnnounce() {
        return doorLeftSideENAnnounce;
    }

    public String getNextKanji() {
        return nextKanji;
    }

    public String getNextEN() {
        return nextEN;
    }

    public String getNextKatakana() {
        return nextKatakana;
    }

    public String getSoonKanji() {
        return soonKanji;
    }

    public String getSoonEN() {
        return soonEN;
    }

    public String getStoppingStationKanji() {
        return stoppingStationKanji;
    }

    public String getStoppingStationEN() {
        return stoppingStationEN;
    }
}
