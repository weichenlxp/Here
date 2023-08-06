package edu.hebut.here.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static edu.hebut.here.data.MyContentResolver.queryCategory;
import static edu.hebut.here.data.MyContentResolver.queryContainer;
import static edu.hebut.here.data.MyContentResolver.queryFurniture;
import static edu.hebut.here.data.MyContentResolver.queryGoods;
import static edu.hebut.here.data.MyContentResolver.queryHouse;
import static edu.hebut.here.data.MyContentResolver.queryRoom;

public class ExcelUtils {

    public static String sqlToExcel(int userID, Context context) {
        List<All> allGoods = new ArrayList<>();
        //从SQLite数据库中读出数据。
        Cursor goods = queryGoods(context, new String[]{"goodsName", "houseID", "roomID", "furnitureID", "categoryID", "containerID", "goodsNum", "buyTime", "manufactureDate", "qualityGuaranteePeriod", "qualityGuaranteePeriodType", "isOvertime", "isCloseOvertime", "packed", "remark"}, "userID=?", new String[]{String.valueOf(userID)});
        for (int i = 1; goods.moveToNext(); i++) {
            All all = new All();
            all.id = i;
            all.goodsName = goods.getString(0);
            Cursor house = queryHouse(context, new String[]{"houseName"}, "houseID=?", new String[]{String.valueOf(goods.getInt(1))});
            house.moveToNext();
            all.houseName = house.getString(0);

            Cursor room = queryRoom(context, new String[]{"roomName"}, "roomID=?", new String[]{String.valueOf(goods.getInt(2))}, null);
            room.moveToNext();
            all.roomName = room.getString(0);

            Cursor furniture = queryFurniture(context, new String[]{"furnitureName"}, "furnitureID=?", new String[]{String.valueOf(goods.getInt(3))});
            furniture.moveToNext();
            all.furnitureName = furniture.getString(0);

            Cursor category = queryCategory(context, new String[]{"categoryName"}, "categoryID=?", new String[]{String.valueOf(goods.getInt(4))});
            category.moveToNext();
            all.categoryName = category.getString(0);

            if (goods.getShort(13) == 0) {
                all.packed = "否";
                all.containerName = "";
            } else {
                all.packed = "是";
                Cursor container = queryContainer(context, new String[]{"containerName"}, "containerID=?", new String[]{String.valueOf(goods.getInt(5))});
                container.moveToNext();
                all.containerName = container.getString(0);
            }
            all.goodsNum = goods.getInt(6);
            all.buyTime = goods.getString(7);
            all.manufactureDate = goods.getString(8);
            all.qualityGuaranteePeriod = goods.getString(9) + goods.getString(10);

            if (goods.getShort(11) == 0) {
                all.isOvertime = "否";
            } else {
                all.isOvertime = "是";
            }

            if (goods.getShort(12) == 0) {
                all.isCloseOvertime = "否";
            } else {
                all.isCloseOvertime = "是";
            }

            all.remark = goods.getString(14);
            allGoods.add(all);
        }

        String fileName = "导出物品表-" + System.currentTimeMillis();
        HSSFWorkbook mWorkbook = new HSSFWorkbook();
        HSSFSheet mSheet = mWorkbook.createSheet(fileName);
        createExcelHead(mSheet);

        for (All all : allGoods) {
            createCell(all.id, all.goodsName, all.houseName, all.roomName, all.furnitureName, all.categoryName, all.packed, all.containerName, all.goodsNum, all.buyTime, all.manufactureDate, all.qualityGuaranteePeriod, all.isOvertime, all.isCloseOvertime, all.remark, mSheet);
        }

        File xlsFile = new File(Environment.getExternalStorageDirectory(), fileName + ".xls");
        try {
            if (!xlsFile.exists()) {
                xlsFile.createNewFile();
            }
            mWorkbook.write(xlsFile);// 或者以流的形式写入文件 mWorkbook.write(new FileOutputStream(xlsFile));
            mWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName + ".xls";
    }

    private static void createExcelHead(HSSFSheet mSheet) {
        HSSFRow headRow = mSheet.createRow(0);
        headRow.createCell(0).setCellValue("ID");
        headRow.createCell(1).setCellValue("物品名称");
        headRow.createCell(2).setCellValue("住所名称");
        headRow.createCell(3).setCellValue("房间名称");
        headRow.createCell(4).setCellValue("家具名称");
        headRow.createCell(5).setCellValue("物品种类");
        headRow.createCell(6).setCellValue("是否打包");
        headRow.createCell(7).setCellValue("容器名称");
        headRow.createCell(8).setCellValue("物品数量");
        headRow.createCell(9).setCellValue("购买时间");
        headRow.createCell(10).setCellValue("生产日期");
        headRow.createCell(11).setCellValue("保质期");
        headRow.createCell(12).setCellValue("是否过期");
        headRow.createCell(13).setCellValue("是否临期");
        headRow.createCell(14).setCellValue("备注");
    }

    private static void createCell(int id, String goodsName, String houseName, String roomName, String furnitureName, String categoryName, String packed, String containerName, int goodsNum, String buyTime, String manufactureDate, String qualityGuaranteePeriod, String isOvertime, String isCloseOvertime, String remark, HSSFSheet sheet) {
        HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
        dataRow.createCell(0).setCellValue(id);
        dataRow.createCell(1).setCellValue(goodsName);
        dataRow.createCell(2).setCellValue(houseName);
        dataRow.createCell(3).setCellValue(roomName);
        dataRow.createCell(4).setCellValue(furnitureName);
        dataRow.createCell(5).setCellValue(categoryName);
        dataRow.createCell(6).setCellValue(packed);
        dataRow.createCell(7).setCellValue(containerName);
        dataRow.createCell(8).setCellValue(goodsNum);
        dataRow.createCell(9).setCellValue(buyTime);
        dataRow.createCell(10).setCellValue(manufactureDate);
        dataRow.createCell(11).setCellValue(qualityGuaranteePeriod);
        dataRow.createCell(12).setCellValue(isOvertime);
        dataRow.createCell(13).setCellValue(isCloseOvertime);
        dataRow.createCell(14).setCellValue(remark);
    }

    private static class All {
        public int id;
        public String goodsName;
        public String houseName;
        public String roomName;
        public String furnitureName;
        public String categoryName;
        public String packed;
        public String containerName;
        public int goodsNum;
        public String buyTime;
        public String manufactureDate;
        public String qualityGuaranteePeriod;
        public String isOvertime;
        public String isCloseOvertime;
        public String remark;
    }
}


