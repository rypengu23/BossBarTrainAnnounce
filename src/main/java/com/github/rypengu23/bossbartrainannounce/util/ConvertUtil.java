package com.github.rypengu23.bossbartrainannounce.util;

import com.github.rypengu23.bossbartrainannounce.config.ConfigLoader;
import com.github.rypengu23.bossbartrainannounce.config.MainConfig;
import com.github.rypengu23.bossbartrainannounce.config.MessageConfig;

import java.util.Locale;
import java.util.regex.Pattern;

public class ConvertUtil {

    private final ConfigLoader configLoader;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;

    public ConvertUtil(){
        configLoader = new ConfigLoader();
        mainConfig = configLoader.getMainConfig();
        messageConfig = configLoader.getMessageConfig();
    }

    public String convertPercentAndColorCode(String str){

        return convertColorCode(convertDoublePercentToSpace(str));
    }

    public String convertDoublePercentToSpace(String str){

        return str.replace("%%", " ");
    }

    public String convertColorCode(String str){

        return str.replace("&", "§");
    }

    public String removeLocalService(String str){

        if(str.toLowerCase().contains(messageConfig.getRemoveWord1())){
            return replaceAll( messageConfig.getRemoveWord1(), "", str);
        }else if(str.toLowerCase().contains(messageConfig.getRemoveWord2())){
            return replaceAll(messageConfig.getRemoveWord2(),"",  str);
        }else{
            return str;
        }
    }

    /**
     * 大文字小文字を区別せずにreplaceAllします
     * @param regex 置き換えたい文字列
     * @param reql 置換後文字列
     * @param text 置換対象文字列
     */
    public String replaceAll(String regex ,String reql,String text){
        String retStr = "";
        retStr = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text).replaceAll(reql);
        return retStr;
    }
}
