package com.leibro.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * Created by leibro on 2017/1/6.
 */
public class ChnCharToPinyin {
    public static String toPinyin(String s) {
        try {
            return PinyinHelper.convertToPinyinString(s,"-", PinyinFormat.WITHOUT_TONE);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return null;
    }
}
