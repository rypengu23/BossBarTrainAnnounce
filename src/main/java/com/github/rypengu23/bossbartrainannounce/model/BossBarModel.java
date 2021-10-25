package com.github.rypengu23.bossbartrainannounce.model;

import org.bukkit.boss.BarColor;

import java.util.ArrayList;

public class BossBarModel {

    private ArrayList<String> messageList;
    private BarColor barColor;
    private Long delayTime;

    public BossBarModel(ArrayList<String> messageList, BarColor barColor, Long delayTime) {
        this.messageList = messageList;
        this.barColor = barColor;
        this.delayTime = delayTime;
    }

    public ArrayList<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public void setBarColor(BarColor barColor) {
        this.barColor = barColor;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }
}
