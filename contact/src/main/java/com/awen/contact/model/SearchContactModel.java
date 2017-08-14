package com.awen.contact.model;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * 搜索联系人算法
 * Created by AwenZeng on 2017/2/16.
 */

public class SearchContactModel {

    /**
     * 模糊搜索（按中文，数字，字母搜索）
     * @param searchStr
     * @param info
     * @return
     */
    public boolean searchContact(String searchStr,ContactsInfo info) {
        return info.getName().contains(searchStr) || info.getPhone().contains(searchStr)||searchByAlphabet(searchStr,info)
                || Pinyin.toPinyin(info.getName(), "").toLowerCase().contains(searchStr);
    }
    /**
     * 按中文首字母
     * @param searchStr
     * @param info
     * @return
     */
    private boolean searchByAlphabet(String searchStr,ContactsInfo info){
        String[] temp = Pinyin.toPinyin(info.getName(), ",").toLowerCase().split(",");
        StringBuilder builder = new StringBuilder();
        for(String str:temp){
            builder.append(str.charAt(0));
        }
        if(builder.toString().contains(searchStr)){
            return true;
        }
        return false;
    }
}
