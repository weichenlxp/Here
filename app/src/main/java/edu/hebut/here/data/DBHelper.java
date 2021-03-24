package edu.hebut.here.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库名
    private static final String DATABASE_NAME = "here.db";

    private static final int DATABASE_VERSION = 1;
    //数据库版本号

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user( _id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL, gender TEXT NOT NULL, profilePhoto MEDIUMBLOB NOT NULL, reminderTime int NOT NULL )");
        db.execSQL("CREATE TABLE IF NOT EXISTS house( _id INTEGER PRIMARY KEY AUTOINCREMENT, houseName TEXT NOT NULL, userID INTEGER NOT NULL, FOREIGN KEY (userID) REFERENCES user (_id) )");
        db.execSQL("CREATE TABLE IF NOT EXISTS account( _id INTEGER PRIMARY KEY AUTOINCREMENT, accountName TEXT NOT NULL,accountValue TEXT NOT NULL, houseID INTEGER NOT NULL, FOREIGN KEY (houseID) REFERENCES house (_id) )");
        db.execSQL("CREATE TABLE IF NOT EXISTS room( _id INTEGER PRIMARY KEY AUTOINCREMENT, roomName TEXT NOT NULL, houseID INTEGER NOT NULL, FOREIGN KEY (houseID) REFERENCES house (_id) )");
        db.execSQL("CREATE TABLE IF NOT EXISTS furniture( _id INTEGER PRIMARY KEY AUTOINCREMENT, furnitureName TEXT NOT NULL, roomID INTEGER NOT NULL, FOREIGN KEY (roomID) REFERENCES room (_id) )");
        db.execSQL("CREATE TABLE IF NOT EXISTS container( _id INTEGER PRIMARY KEY AUTOINCREMENT, containerName TEXT NOT NULL, userID INTEGER NOT NULL, FOREIGN KEY (userID) REFERENCES user (_id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS category( _id INTEGER PRIMARY KEY AUTOINCREMENT, categoryName TEXT NOT NULL, userID INTEGER NOT NULL, FOREIGN KEY (userID) REFERENCES user (_id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS goods( _id INTEGER PRIMARY KEY AUTOINCREMENT, goodsName TEXT NOT NULL, userID INTEGER NOT NULL, houseID INTEGER NOT NULL, roomID INTEGER NOT NULL, furnitureID INTEGER NOT NULL, categoryID INTEGER NOT NULL, containerID INTEGER, goodsNum INTEGER, goodsPhoto1 MEDIUMBLOB, goodsPhoto2 MEDIUMBLOB, goodsPhoto3 MEDIUMBLOB, buyTime TEXT, manufactureDate TEXT, qualityGuaranteePeriod INTEGER ,qualityGuaranteePeriodType TEXT,  overtime TEXT, isOvertime BOOLEAN NOT NULL, isCloseOvertime BOOLEAN NOT NULL, packed BOOLEAN NOT NULL, remark TEXT, FOREIGN KEY (userID) REFERENCES user (_id), FOREIGN KEY (houseID) REFERENCES house (_id), FOREIGN KEY (roomID) REFERENCES room (_id), FOREIGN KEY (furnitureID) REFERENCES furniture (_id), FOREIGN KEY (categoryID) REFERENCES category (_id), FOREIGN KEY (containerID) REFERENCES container (_id) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}