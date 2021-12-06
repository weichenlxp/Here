package edu.hebut.here.utils;

import android.util.Log;

import java.nio.charset.StandardCharsets;

public class StringUtils {
    // 把字节数组转化为字符串----"ISO-8859-1"
    public static String byteToString(byte[] data){
        String dataString=null;
        try{
            //将字节数组转为字符串，编码格式为ISO-8859-1
            dataString=new String(data, StandardCharsets.UTF_8);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dataString;
    }
    // 把字符串转化为字节数组----"ISO-8859-1"
    public static byte[] stringToByte(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }
}
