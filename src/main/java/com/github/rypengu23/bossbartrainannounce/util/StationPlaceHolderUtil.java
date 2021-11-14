package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.dao.LineDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.LineModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;

import java.util.ArrayList;

public class StationPlaceHolderUtil {

    private LineModel lineModel;
    private StationModel stationModel;
    private ArrayList<LineModel> transferLineList;

    private String transferJP;
    private String transferEN;


    public StationPlaceHolderUtil(LineModel lineModel, StationModel stationModel) {

        this.lineModel = lineModel;
        this.stationModel = stationModel;

        //乗り換え先リストを取得
        this.transferLineList = getTransferList(stationModel);
        //乗り換え先リストを変換
        if (transferLineList.size() != 0) {
            StringBuilder lineMessageJP = new StringBuilder();
            StringBuilder lineMessageEN = new StringBuilder();

            for (int i = 0; i < transferLineList.size(); i++) {
                if (i == 0) {
                    lineMessageEN.append("the ");
                } else if (i + 1 != transferLineList.size()) {
                    lineMessageJP.append("、");
                    lineMessageEN.append(", the ");
                } else {
                    lineMessageJP.append("、");
                    lineMessageEN.append(" and the ");
                }
                lineMessageJP.append(transferLineList.get(i).getLineNameJP());
                lineMessageEN.append(transferLineList.get(i).getLineNameEN());
            }
            transferJP = lineMessageJP.toString();
            transferEN = lineMessageEN.toString();
        }

    }

    /**
     * 置き換え後のMessageConfigを作成
     * @return
     */
    public String replaceMessageConfig(String str){

        return placeHolderAnnounce(str);
    }

    /**
     * アナウンス文章を置き換える
     * @param word
     * @return
     */
    public String placeHolderAnnounce(String word) {

        word = word.replace("{LineNameJP}", lineModel.getLineNameJP());
        word = word.replace("{LineNameEN}", lineModel.getLineNameEN());

        word = word.replace("{StationNameJP}", stationModel.getStationNameKanji());
        word = word.replace("{StationNameEN}", stationModel.getStationNameEnglish());
        word = word.replace("{StationNameKatakana}", stationModel.getStationNameKatakana());

        if(checkTransferExit()) {
            word = word.replace("{TransferJP}", transferJP);
            word = word.replace("{TransferEN}", transferEN);
        }else{
            word = word.replace("{TransferJP}", "");
            word = word.replace("{TransferEN}", "");
        }

        if(stationModel.getNumber() != null) {
            word = word.replace("{Number}", stationModel.getNumber());
            word = word.replace("{BracketsNumber}", "("+ stationModel.getNumber() +")");
        }else{
            word = word.replace("{Number}", "");
            word = word.replace("{BracketsNumber}", "");
        }

        return word;
    }

    /**
     * 乗り換え先の有無を確認
     * @return
     */
    public boolean checkTransferExit(){

        if(transferLineList == null){
            return false;
        }
        if(transferLineList.size() != 0){
            return true;
        }
        return false;
    }

    /**
     * 乗り換え先を取得
     * @param stationModel
     * @return
     */
    public ArrayList<LineModel> getTransferList(StationModel stationModel){

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();
        int posX = (stationModel.getSelectPositionModel().getPos1X() + stationModel.getSelectPositionModel().getPos1X()) / 2;
        int posY = (stationModel.getSelectPositionModel().getPos1Y() + stationModel.getSelectPositionModel().getPos1Y()) / 2;
        int posZ = (stationModel.getSelectPositionModel().getPos1Z() + stationModel.getSelectPositionModel().getPos1Z()) / 2;

        //乗り換え先取得
        ArrayList<ArrayList<String>> transferList = stationDao.getLineBelongsStation(stationModel.getStationNameKanji(), posX, posY, posZ, stationModel.getLineNameJp());

        ArrayList<LineModel> resultList = new ArrayList<>();
        for(ArrayList<String> lineInfo:transferList){
            if(lineInfo.size() != 2){
                continue;
            }
            resultList.add(lineDao.getLine(lineInfo.get(0), lineInfo.get(1)));
        }

        return resultList;
    }
}
