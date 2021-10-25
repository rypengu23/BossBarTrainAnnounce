package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;
import com.github.rypengu23.bossbartrainannounce.dao.LineDao;
import com.github.rypengu23.bossbartrainannounce.dao.StationDao;
import com.github.rypengu23.bossbartrainannounce.model.AnnounceInfoModel;
import com.github.rypengu23.bossbartrainannounce.model.LineModel;
import com.github.rypengu23.bossbartrainannounce.model.StationModel;

import java.util.ArrayList;

public class AnnouncePlaceHolderUtil {

    private LineModel lineModel;
    private LineModel viaLineModel;
    private StationModel stationModel;
    private AnnounceInfoModel announceInfoModel;
    private ArrayList<LineModel> transferLineList;

    private String transferJP;
    private String transferEN;

    private String typeJP;
    private String typeEN;

    public AnnouncePlaceHolderUtil(LineModel lineModel, StationModel stationModel, AnnounceInfoModel announceInfoModel){

        this.lineModel = lineModel;
        this.stationModel = stationModel;
        this.announceInfoModel = announceInfoModel;

        //直通先情報を取得
        LineDao lineDao = new LineDao();
        if(announceInfoModel.getViaLineNameJP() != null) {
            this.viaLineModel = lineDao.getLine(lineModel.getUUID(), announceInfoModel.getViaLineNameJP());
        }else{
            this.viaLineModel = null;
        }

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

        //種別を取得
        this.typeJP = lineDao.getTypeJP(lineModel.getUUID(), lineModel.getLineNameJP(), announceInfoModel.getTypeJP());
        this.typeEN = lineDao.getTypeEN(lineModel.getUUID(), lineModel.getLineNameJP(), announceInfoModel.getTypeJP());
    }

    /**
     * 置き換え後のメッセージを作成
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

        ConvertUtil convertUtil = new ConvertUtil();

        word = word.replace("{LineNameJP}", lineModel.getLineNameJP());
        word = word.replace("{LineNameEN}", lineModel.getLineNameEN());

        if(viaLineModel != null) {
            word = word.replace("{ViaLineNameJP}", viaLineModel.getLineNameJP());
            word = word.replace("{ViaLineNameEN}", viaLineModel.getLineNameEN());
        }else{
            word = word.replace("{ViaLineNameJP}", "");
            word = word.replace("{ViaLineNameEN}", "");
        }

        if(typeJP != null && typeEN != null) {
            word = word.replace("{TypeJP}", typeJP);
            word = word.replace("{TypeEN}", typeEN);
        }else{
            word = word.replace("{TypeJP}", "");
            word = word.replace("{TypeEN}", "");
        }

        if(announceInfoModel.getBoundJP() != null && announceInfoModel.getBoundEN() != null) {
            word = word.replace("{BoundJP}", announceInfoModel.getBoundJP());
            word = word.replace("{BoundEN}", announceInfoModel.getBoundEN());
        }else{
            word = word.replace("{BoundJP}", "");
            word = word.replace("{BoundEN}", "");
        }

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

        word = convertUtil.removeLocalService(word);

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
        if(transferLineList.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 乗り換え先を取得
     * @param stationModel
     * @return
     */
    public ArrayList<LineModel> getTransferList(StationModel stationModel){

        StationDao stationDao = new StationDao();
        LineDao lineDao = new LineDao();
        int posX = (stationModel.getSelectPositionModel().getPos1X() + stationModel.getSelectPositionModel().getPos2X()) / 2;
        int posY = (stationModel.getSelectPositionModel().getPos1Y() + stationModel.getSelectPositionModel().getPos2Y()) / 2;
        int posZ = (stationModel.getSelectPositionModel().getPos1Z() + stationModel.getSelectPositionModel().getPos2Z()) / 2;

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
