package edu.hebut.here.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipUtils {
    // 压缩字符串----"ISO-8859-1"
    private String compress(String data){
        String finalData=null;
        try{
            if (data == null || data.length() == 0) {
                return data;
            }
            //打开字节输出流
            ByteArrayOutputStream bout=new ByteArrayOutputStream();
            //打开压缩用的输出流,压缩后的结果放在bout中
            GZIPOutputStream gout=new GZIPOutputStream(bout);
            //写入待压缩的字节数组
            gout.write(data.getBytes(StandardCharsets.UTF_8));
            //完成压缩写入
            gout.finish();
            //关闭输出流
            gout.close();
            finalData=bout.toString("UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        return finalData;
    }
    // 解压缩
    public static String uncompress(String data) {
        String finalData=null;
        try{
            //打开字节输出流
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ByteArrayInputStream bin = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            //打开压缩用的输出流,压缩后的结果放在bout中
            GZIPInputStream gin=new GZIPInputStream(bin);
            //写入待压缩的字节数组
            byte[] buffer = new byte[2048];
            int n;
            while ((n = gin.read(buffer)) >= 0) {
                bout.write(buffer, 0, n);
            }
            finalData=bout.toString("UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        return finalData;
    }
}
