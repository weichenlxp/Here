package edu.hebut.here.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class MyContentResolver {
    public static Uri uri_user = Uri.parse("content://edu.hebut.here.data/user");
    public static Uri uri_house = Uri.parse("content://edu.hebut.here.data/house");
    public static Uri uri_account = Uri.parse("content://edu.hebut.here.data/account");
    public static Uri uri_room = Uri.parse("content://edu.hebut.here.data/room");
    public static Uri uri_furniture = Uri.parse("content://edu.hebut.here.data/furniture");
    public static Uri uri_container = Uri.parse("content://edu.hebut.here.data/container");
    public static Uri uri_category = Uri.parse("content://edu.hebut.here.data/category");
    public static Uri uri_goods = Uri.parse("content://edu.hebut.here.data/goods");

    public static void createUser(Context context, String username, String password, String gender, byte[] profilePhoto, int reminderTime) {
        ContentValues user = new ContentValues();
        user.put("username", username);
        user.put("password", password);
        user.put("gender", gender);
        user.put("profilePhoto", profilePhoto);
        user.put("reminderTime", reminderTime);
        context.getContentResolver().insert(uri_user, user);
    }

    public static Cursor queryUser(Context context, String[] projection, String selection, String[] selectionArgs) {
        return context.getContentResolver().query(uri_user, projection, selection, selectionArgs, null);
    }

    public static void updateUserProfilePhoto(Context context, byte[] profilePhoto, int userID) {
        ContentValues user = new ContentValues();
        user.put("profilePhoto", profilePhoto);
        context.getContentResolver().update(uri_user, user, "userID=?", new String[]{String.valueOf(userID)});
    }

    public static void updateUserGender(Context context, String gender, int userID) {
        ContentValues user = new ContentValues();
        user.put("gender", gender);
        context.getContentResolver().update(uri_user, user, "userID=?", new String[]{String.valueOf(userID)});
    }

    public static void updateUserReminderTime(Context context, int reminderTime, int userID) {
        ContentValues user = new ContentValues();
        user.put("reminderTime", reminderTime);
        context.getContentResolver().update(uri_user, user, "userID=?", new String[]{String.valueOf(userID)});
    }

    public static void createHouse(Context context, String houseName, int userID) {
        ContentValues house = new ContentValues();
        house.put("houseName", houseName);
        house.put("userID", userID);
        context.getContentResolver().insert(uri_house, house);
    }

    public static int deleteHouse(Context context, String where, String[] selectionArgs) {
        return context.getContentResolver().delete(uri_house, where, selectionArgs);
    }

    public static void updateHouseName(Context context, String newHouseName, String houseName, int userID) {
        ContentValues house = new ContentValues();
        house.put("houseName", newHouseName);
        context.getContentResolver().update(uri_house, house, "houseName=? AND userID=?", new String[]{houseName, String.valueOf(userID)});
    }

    public static Cursor queryHouse(Context context, String[] projection, String selection, String[] selectionArgs) {
        return context.getContentResolver().query(uri_house, projection, selection, selectionArgs, null);
    }

    public static void createAccount(Context context, String accountName, int houseID) {
        ContentValues account = new ContentValues();
        account.put("accountName", accountName);
        account.put("accountValue", "请设置您的账号");
        account.put("houseID", houseID);
        context.getContentResolver().insert(uri_account, account);
    }

    public static Cursor queryAccount(Context context, String[] projection, String selection, String[] selectionArgs) {
        return context.getContentResolver().query(uri_account, projection, selection, selectionArgs, null);
    }

    public static void updateAccountValue(Context context, String accountValue, int houseID, String accountName) {
        ContentValues account = new ContentValues();
        account.put("accountValue", accountValue);
        context.getContentResolver().update(uri_account, account, "houseID=? and accountName=?", new String[]{String.valueOf(houseID), accountName});
    }

    public static void createRoom(Context context, String roomName, int houseID, int userID) {
        ContentValues room = new ContentValues();
        room.put("roomName", roomName);
        room.put("houseID", houseID);
        room.put("userID", userID);
        context.getContentResolver().insert(uri_room, room);
    }

    public static Cursor queryRoom(Context context, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return context.getContentResolver().query(uri_room, projection, selection, selectionArgs, sortOrder);
    }

    public static void updateRoomName(Context context, String newRoomName, int houseID, int roomID) {
        ContentValues room = new ContentValues();
        room.put("roomName", newRoomName);
        room.put("houseID", houseID);
        context.getContentResolver().update(uri_room, room, "roomID=?", new String[]{String.valueOf(roomID)});
    }

    public static int deleteRoom(Context context, String where, String[] selectionArgs) {
        return context.getContentResolver().delete(uri_room, where, selectionArgs);
    }

    public static void createFurniture(Context context, String furnitureName, int userID, int houseID, int roomID) {
        ContentValues furniture = new ContentValues();
        furniture.put("furnitureName", furnitureName);
        furniture.put("userID", userID);
        furniture.put("houseID", houseID);
        furniture.put("roomID", roomID);
        context.getContentResolver().insert(uri_furniture, furniture);
    }

//    public static void createFurniture(Context context, String furnitureName, byte[] furniturePic, int userID, int houseID, int roomID) {
//        ContentValues furniture = new ContentValues();
//        furniture.put("furnitureName", furnitureName);
//        furniture.put("furniturePic", furniturePic);
//        furniture.put("userID", userID);
//        furniture.put("houseID", houseID);
//        furniture.put("roomID", roomID);
//        context.getContentResolver().insert(uri_furniture, furniture);
//    }

    public static Cursor queryFurniture(Context context, String[] projection, String selection, String[] selectionArgs) {
        return context.getContentResolver().query(uri_furniture, projection, selection, selectionArgs, null);
    }

    public static void updateFurnitureName(Context context, String newFurnitureName, int houseID, int roomID, int furnitureID) {
        ContentValues furniture = new ContentValues();
        furniture.put("furnitureName", newFurnitureName);
        furniture.put("houseID", houseID);
        furniture.put("roomID", roomID);
        context.getContentResolver().update(uri_furniture, furniture, "furnitureID=?", new String[]{String.valueOf(furnitureID)});
    }

    public static int deleteFurniture(Context context, String where, String[] selectionArgs) {
        return context.getContentResolver().delete(uri_furniture, where, selectionArgs);
    }

    public static void createContainer(Context context, String containerName, int userID) {
        ContentValues container = new ContentValues();
        container.put("containerName", containerName);
        container.put("userID", userID);
        context.getContentResolver().insert(uri_container, container);
    }

    public static Cursor queryContainer(Context context, String[] projection, String selection, String[] selectionArgs) {
        return context.getContentResolver().query(uri_container, projection, selection, selectionArgs, null);
    }

    public static void updateContainerName(Context context, String newContainerName, String containerName, int userID) {
        ContentValues container = new ContentValues();
        container.put("containerName", newContainerName);
        context.getContentResolver().update(uri_container, container, "containerName=? AND userID=?", new String[]{containerName, String.valueOf(userID)});
    }

    public static int deleteContainer(Context context, String where, String[] selectionArgs) {
        return context.getContentResolver().delete(uri_container, where, selectionArgs);
    }

    public static void createCategory(Context context, String categoryName, int userID) {
        ContentValues category = new ContentValues();
        category.put("categoryName", categoryName);
        category.put("userID", userID);
        context.getContentResolver().insert(uri_category, category);
    }

    public static Cursor queryCategory(Context context, String[] projection, String selection, String[] selectionArgs) {
        return context.getContentResolver().query(uri_category, projection, selection, selectionArgs, null);
    }

    public static void updateCategory(Context context, String newCategoryName, String categoryName, int userID) {
        ContentValues category = new ContentValues();
        category.put("categoryName", newCategoryName);
        context.getContentResolver().update(uri_category, category, "categoryName=? AND userID=?", new String[]{categoryName, String.valueOf(userID)});
    }

    public static int deleteCategory(Context context, String where, String[] selectionArgs) {
        return context.getContentResolver().delete(uri_category, where, selectionArgs);
    }

    public static void createGoods(Context context, String goodsName, int userID, int houseID, int roomID, int furnitureID, int categoryID, int containerID, int goodsNum, byte[] goodsPhoto1, byte[] goodsPhoto2, byte[] goodsPhoto3, String buyTime, String manufactureDate, String qualityGuaranteePeriod, String qualityGuaranteePeriodType, String overtime, boolean isOvertime, boolean isCloseOvertime, boolean packed, String remark) {
        ContentValues goods = new ContentValues();
        goods.put("goodsName", goodsName);
        goods.put("userID", userID);
        goods.put("houseID", houseID);
        goods.put("roomID", roomID);
        goods.put("furnitureID", furnitureID);
        goods.put("categoryID", categoryID);
        goods.put("containerID", containerID);
        goods.put("goodsNum", goodsNum);
        goods.put("goodsPhoto1", goodsPhoto1);
        goods.put("goodsPhoto2", goodsPhoto2);
        goods.put("goodsPhoto3", goodsPhoto3);
        goods.put("buyTime", buyTime);
        goods.put("manufactureDate", manufactureDate);
        goods.put("qualityGuaranteePeriod", qualityGuaranteePeriod);
        goods.put("qualityGuaranteePeriodType", qualityGuaranteePeriodType);
        goods.put("overtime", overtime);
        goods.put("isCloseOvertime", isCloseOvertime);
        goods.put("isOvertime", isOvertime);
        goods.put("packed", packed);
        goods.put("remark", remark);
        context.getContentResolver().insert(uri_goods, goods);
    }

    public static Cursor queryGoods(Context context, String[] projection, String selection, String[] selectionArgs) {
        return context.getContentResolver().query(uri_goods, projection, selection, selectionArgs, null);
    }

    public static int deleteGoods(Context context, String where, String[] selectionArgs) {
        return context.getContentResolver().delete(uri_goods, where, selectionArgs);
    }

    public static void updateGoods(Context context, int goodsID, String goodsName, int userID, int houseID, int roomID, int furnitureID, int categoryID, int containerID, int goodsNum, byte[] goodsPhoto1, byte[] goodsPhoto2, byte[] goodsPhoto3, String buyTime, String manufactureDate, String qualityGuaranteePeriod, String qualityGuaranteePeriodType, String overtime, boolean isOvertime, boolean isCloseOvertime, boolean packed, String remark) {
        ContentValues goods = new ContentValues();
        goods.put("goodsName", goodsName);
        goods.put("userID", userID);
        goods.put("houseID", houseID);
        goods.put("roomID", roomID);
        goods.put("furnitureID", furnitureID);
        goods.put("categoryID", categoryID);
        goods.put("containerID", containerID);
        goods.put("goodsNum", goodsNum);
        goods.put("goodsPhoto1", goodsPhoto1);
        goods.put("goodsPhoto2", goodsPhoto2);
        goods.put("goodsPhoto3", goodsPhoto3);
        goods.put("buyTime", buyTime);
        goods.put("manufactureDate", manufactureDate);
        goods.put("qualityGuaranteePeriod", qualityGuaranteePeriod);
        goods.put("qualityGuaranteePeriodType", qualityGuaranteePeriodType);
        goods.put("overtime", overtime);
        goods.put("isCloseOvertime", isCloseOvertime);
        goods.put("isOvertime", isOvertime);
        goods.put("packed", packed);
        goods.put("remark", remark);
        context.getContentResolver().update(uri_goods, goods, "goodsID=?", new String[]{String.valueOf(goodsID)});
    }

    public static void updateGoods(Context context, int goodsID, String manufactureDate, String qualityGuaranteePeriod, String qualityGuaranteePeriodType, String overtime, boolean isOvertime, boolean isCloseOvertime) {
        ContentValues goods = new ContentValues();
        goods.put("manufactureDate", manufactureDate);
        goods.put("qualityGuaranteePeriod", qualityGuaranteePeriod);
        goods.put("qualityGuaranteePeriodType", qualityGuaranteePeriodType);
        goods.put("overtime", overtime);
        goods.put("isCloseOvertime", isCloseOvertime);
        goods.put("isOvertime", isOvertime);
        context.getContentResolver().update(uri_goods, goods, "goodsID=?", new String[]{String.valueOf(goodsID)});
    }
}
