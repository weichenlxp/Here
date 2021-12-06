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
    public void onOpen(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints 开启外键约束
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user( userID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL, gender TEXT NOT NULL, profilePhoto MEDIUMBLOB NOT NULL, reminderTime int NOT NULL )");
        db.execSQL("CREATE TABLE IF NOT EXISTS house( houseID INTEGER PRIMARY KEY AUTOINCREMENT, houseName TEXT NOT NULL, userID INTEGER NOT NULL, FOREIGN KEY (userID) REFERENCES user (userID) ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS account( accountID INTEGER PRIMARY KEY AUTOINCREMENT, accountName TEXT NOT NULL,accountValue TEXT NOT NULL, houseID INTEGER NOT NULL, FOREIGN KEY (houseID) REFERENCES house (houseID) ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS room( roomID INTEGER PRIMARY KEY AUTOINCREMENT, roomName TEXT NOT NULL, userID INTEGER NOT NULL, houseID INTEGER NOT NULL, FOREIGN KEY (userID) REFERENCES user (userID) ON DELETE CASCADE, FOREIGN KEY (houseID) REFERENCES house (houseID) ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS furniture( furnitureID INTEGER PRIMARY KEY AUTOINCREMENT, furnitureName TEXT NOT NULL, userID INTEGER NOT NULL, houseID INTEGER NOT NULL, roomID INTEGER NOT NULL, FOREIGN KEY (userID) REFERENCES user (userID) ON DELETE CASCADE, FOREIGN KEY (houseID) REFERENCES house (houseID) ON DELETE CASCADE, FOREIGN KEY (roomID) REFERENCES room (roomID) ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS container( containerID INTEGER PRIMARY KEY AUTOINCREMENT, containerName TEXT NOT NULL, userID INTEGER NOT NULL, FOREIGN KEY (userID) REFERENCES user (userID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS category( categoryID INTEGER PRIMARY KEY AUTOINCREMENT, categoryName TEXT NOT NULL, userID INTEGER NOT NULL, FOREIGN KEY (userID) REFERENCES user (userID))");
        db.execSQL("CREATE TABLE IF NOT EXISTS goods( goodsID INTEGER PRIMARY KEY AUTOINCREMENT, goodsName TEXT NOT NULL, userID INTEGER NOT NULL, houseID INTEGER NOT NULL, roomID INTEGER NOT NULL, furnitureID INTEGER NOT NULL, categoryID INTEGER NOT NULL, containerID INTEGER, goodsNum INTEGER, goodsPhoto1 MEDIUMBLOB, goodsPhoto2 MEDIUMBLOB, goodsPhoto3 MEDIUMBLOB, buyTime TEXT, manufactureDate TEXT, qualityGuaranteePeriod INTEGER ,qualityGuaranteePeriodType TEXT,  overtime TEXT, isOvertime BOOLEAN NOT NULL, isCloseOvertime BOOLEAN NOT NULL, packed BOOLEAN NOT NULL, remark TEXT, FOREIGN KEY (userID) REFERENCES user (userID) ON DELETE CASCADE, FOREIGN KEY (houseID) REFERENCES house (houseID) ON DELETE CASCADE, FOREIGN KEY (roomID) REFERENCES room (roomID) ON DELETE CASCADE, FOREIGN KEY (furnitureID) REFERENCES furniture (furnitureID) ON DELETE CASCADE, FOREIGN KEY (categoryID) REFERENCES category (categoryID) ON DELETE CASCADE )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}