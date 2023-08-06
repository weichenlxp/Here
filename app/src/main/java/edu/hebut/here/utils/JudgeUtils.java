package edu.hebut.here.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JudgeUtils {
    public static boolean judgeExist(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        return cursor != null && cursor.getCount() > 0;
    }

    public static boolean judgeUsername(String username) {
        String pattern = "^[\\u4E00-\\u9FA5A-Za-z0-9]{4,12}+$";//包含汉字字母数字，长度在4~12之间
        return Pattern.matches(pattern, username);
    }

    public static boolean judgePassword(String password) {
        String pattern = "^[a-zA-Z0-9_]{6,16}$";//长度在6~16之间，只能包含字符、数字和下划线
        return Pattern.matches(pattern, password);
    }

    public static String getNumeric(String str) {
        String regEx="[^0-9-]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}