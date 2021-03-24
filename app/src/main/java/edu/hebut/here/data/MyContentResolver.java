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

    public static void createHouse(Context context, String houseName, int userID) {
        ContentValues house = new ContentValues();
        house.put("houseName", houseName);
        house.put("userID", userID);
        context.getContentResolver().insert(uri_house, house);
    }

    public static void createAccount(Context context, String accountName, int houseID) {
        ContentValues account = new ContentValues();
        account.put("accountName", accountName);
        account.put("accountValue", "请设置您的账号");
        account.put("houseID", houseID);
        context.getContentResolver().insert(uri_account, account);
    }

    public static void createRoom(Context context, String roomName, int houseID) {
        ContentValues room = new ContentValues();
        room.put("roomName", roomName);
        room.put("houseID", houseID);
        context.getContentResolver().insert(uri_room, room);
    }

    public static void createFurniture(Context context, String furnitureName, int roomID) {
        ContentValues furniture = new ContentValues();
        furniture.put("furnitureName", furnitureName);
        furniture.put("roomID", roomID);
        context.getContentResolver().insert(uri_furniture, furniture);
    }

    public static void createContainer(Context context, String containerName, int userID) {
        ContentValues container = new ContentValues();
        container.put("containerName", containerName);
        container.put("userID", userID);
        context.getContentResolver().insert(uri_container, container);
    }

    public static void createCategory(Context context, String categoryName, int userID) {
        ContentValues category = new ContentValues();
        category.put("categoryName", categoryName);
        category.put("userID", userID);
        context.getContentResolver().insert(uri_category, category);
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

    public static Cursor queryUserIDByUsername(Context context, String username){
        return context.getContentResolver().query(uri_user, new String[]{"_id"}, "username=?", new String[]{username}, null);
    }

    public static Cursor queryUserByUserID(Context context, int userID, String[] projection){
        return context.getContentResolver().query(uri_user, projection, "_id=?", new String[]{String.valueOf(userID)}, null);
    }

    public static Cursor queryHouseIDByUserID(Context context, int userID){
        return context.getContentResolver().query(uri_house, new String[]{"_id"}, "userID=?", new String[]{String.valueOf(userID)}, null);
    }

    public static Cursor queryHouseByUserID(Context context, int userID, String[] projection){
        return context.getContentResolver().query(uri_house, projection, "userID=?", new String[]{String.valueOf(userID)}, null);
    }

    public static Cursor queryAccountByHouseID(Context context, int houseID, String[] projection){
        return context.getContentResolver().query(uri_account, projection, "houseID=?", new String[]{String.valueOf(houseID)}, null);
    }

    public static void updateAccountByHouseIDAndName(Context context,String accountValue, int houseID, String accountName){
        ContentValues account = new ContentValues();
        account.put("accountValue", accountValue);
        context.getContentResolver().update(uri_account, account, "houseID=? and accountName=?", new String[]{String.valueOf(houseID), accountName});
    }

    public static Cursor queryRoomByHouseID(Context context, int houseID, String[] projection){
        return context.getContentResolver().query(uri_room, projection, "houseID=?", new String[]{String.valueOf(houseID)}, null);
    }

    public static Cursor queryRoomIDByRoomNameHouseID(Context context, String roomName, int houseID){
        return context.getContentResolver().query(uri_room, new String[]{"_id"}, "roomName=? AND houseID=?", new String[]{roomName, String.valueOf(houseID)}, null);
    }
    public static Cursor queryFurnitureIDByFurnitureNameRoomID(Context context, String furnitureName, int roomID){
        return context.getContentResolver().query(uri_furniture, new String[]{"_id"}, "furnitureName=? AND roomID=?", new String[]{furnitureName, String.valueOf(roomID)}, null);
    }

    public static Cursor queryContainerIDByContainerNameUserID(Context context, String containerName, int userID){
        return context.getContentResolver().query(uri_container, new String[]{"_id"}, "containerName=? AND userID=?", new String[]{containerName, String.valueOf(userID)}, null);
    }

    public static Cursor queryCategoryIDByCategoryNameUserID(Context context, String categoryName, int userID){
        return context.getContentResolver().query(uri_category, new String[]{"_id"}, "categoryName=? AND userID=?", new String[]{categoryName, String.valueOf(userID)}, null);
    }

    public static Cursor queryOvertimeGoods(Context context, int userID, String[] projection){
        return context.getContentResolver().query(uri_goods, projection, "userID=? AND isOvertime=1", new String[]{String.valueOf(userID)}, null);
    }

    public static Cursor queryCloseOvertimeGoods(Context context, int userID, String[] projection){
        return context.getContentResolver().query(uri_goods, projection, "userID=? AND isCloseOvertime=1", new String[]{String.valueOf(userID)}, null);
    }

    public static void updateHouseNameByHouseName(Context context, String newHouseName, String houseName) {
        ContentValues house = new ContentValues();
        house.put("houseName", newHouseName);
        context.getContentResolver().update(uri_house, house, "houseName=?", new String[]{houseName});
    }

    public static int deleteHouseByHouseNameUserID(Context context, String houseName, int userID) {
        return context.getContentResolver().delete(uri_house, "houseName=? AND userID=?", new String[]{houseName, String.valueOf(userID)});
    }

    public static Cursor queryHouseIDByHouseNameUserID(Context context, String houseName, int userID) {
        return context.getContentResolver().query(uri_house, new String[]{"_id"}, "houseName=? AND userID=?", new String[]{houseName, String.valueOf(userID)}, null);
    }

    public static void updateRoomNameByRoomName(Context context, String newRoomName, String roomName) {
        ContentValues room = new ContentValues();
        room.put("roomName", newRoomName);
        context.getContentResolver().update(uri_room, room, "roomName=?", new String[]{roomName});
    }

    public static int deleteRoomByRoomNameHouseID(Context context, String houseName, int userID) {
        return context.getContentResolver().delete(uri_room, "houseName=? AND userID=?", new String[]{houseName, String.valueOf(userID)});
    }

    public static Cursor queryContainerByUserID(Context context, int userID, String[] projection){
        return context.getContentResolver().query(uri_container, projection, "userID=?", new String[]{String.valueOf(userID)}, null);
    }

    public static void updateContainerNameByContainerName(Context context, String newContainerName, String containerName) {
        ContentValues container = new ContentValues();
        container.put("containerName", newContainerName);
        context.getContentResolver().update(uri_container, container, "containerName=?", new String[]{containerName});
    }

    public static int deleteContainerByContainerNameUserID(Context context, String containerName, int userID) {
        return context.getContentResolver().delete(uri_container, "containerName=? AND userID=?", new String[]{containerName, String.valueOf(userID)});
    }

    public static Cursor queryCategoryByUserID(Context context, int userID, String[] projection){
        return context.getContentResolver().query(uri_category, projection, "userID=?", new String[]{String.valueOf(userID)}, null);
    }

    public static void updateCategoryNameByCategoryName(Context context, String newCategoryName, String categoryName) {
        ContentValues category = new ContentValues();
        category.put("categoryName", newCategoryName);
        context.getContentResolver().update(uri_category, category, "categoryName=?", new String[]{categoryName});
    }

    public static int deleteCategoryByCategoryNameUserID(Context context, String categoryName, int userID) {
        return context.getContentResolver().delete(uri_category, "categoryName=? AND userID=?", new String[]{categoryName, String.valueOf(userID)});
    }
}
