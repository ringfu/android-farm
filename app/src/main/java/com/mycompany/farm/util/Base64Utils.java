package com.mycompany.farm.util;

import android.util.Base64;
//import Decoder.BASE64Decoder;
//import Decoder.BASE64Encoder;

public class Base64Utils {


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) {
        int decodetime = 5;//压缩和解压的次数，防止被简单破解
        byte[] bt = new byte[0];
        key = key.trim().replace(" ", "");//去掉空格
            while (decodetime > 0) {
//                bt = (new BASE64Decoder()).decodeBuffer(key);
//                key = new String(bt);
                // String strBase64 = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
                //key = Base64.encodeToString(bt,Base64.DEFAULT);
                // tring str2 = new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT));
                key = new String(Base64.decode(bt, Base64.DEFAULT));
                decodetime--;
            }
            return key;//如果出现乱码可以改成： String(bt, "utf-8")或 gbk
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) {
        int decodetime = 5;//压缩和解压的次数，防止被简单破解
        byte[] bt = null;
        key = key.trim().replace(" ", "");//去掉空格
        while (decodetime > 0) {
            bt = key.getBytes();
            //key = (new BASE64Encoder()).encodeBuffer(bt);
            key = Base64.encodeToString(bt,Base64.DEFAULT);
            decodetime--;
        }

        return key;
    }

}
