package edu.hebut.here.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final String authorities = "edu.hebut.here.data";
    private static final int User = 1;
    private static final int House = 2;
    private static final int Account = 3;
    private static final int Room = 4;
    private static final int Furniture = 5;
    private static final int Container = 6;
    private static final int Category = 7;
    private static final int Goods = 8;
    private static final UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(authorities, "user", User);
        matcher.addURI(authorities, "house", House);
        matcher.addURI(authorities, "account", Account);
        matcher.addURI(authorities, "room", Room);
        matcher.addURI(authorities, "furniture", Furniture);
        matcher.addURI(authorities, "container", Container);
        matcher.addURI(authorities, "category", Category);
        matcher.addURI(authorities, "goods", Goods);
    }

    DBHelper mDbHelper = null;
    SQLiteDatabase db = null;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        // 在ContentProvider创建时对数据库进行初始化
        // 运行在主线程，故不能做耗时操作,此处仅作展示
        mDbHelper = new DBHelper(getContext());
        db = mDbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(getTableName(uri), null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return db.query(getTableName(uri), projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(getTableName(uri), values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete(getTableName(uri), selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (matcher.match(uri)) {
            case User:
                tableName = "user";
                break;
            case House:
                tableName = "house";
                break;
            case Account:
                tableName = "account";
                break;
            case Room:
                tableName = "room";
                break;
            case Furniture:
                tableName = "furniture";
                break;
            case Container:
                tableName = "container";
                break;
            case Category:
                tableName = "category";
                break;
            case Goods:
                tableName = "goods";
                break;
        }
        return tableName;
    }
}