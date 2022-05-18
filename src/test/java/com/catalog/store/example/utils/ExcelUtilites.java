package com.catalog.store.example.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUtilites {

    /**
     * Apache POI - Get Workbook (XLS/XLSX)
     * @param filePath
     * @return
     */
    public static Workbook getWorkbook(String filePath){
        try {
            FileInputStream fis = new FileInputStream(filePath);
            String suffix = filePath.split("\\.")[1];

            Workbook workbook = null;
            if ("xlsx".equals(suffix)){
                workbook = new XSSFWorkbook(fis);
            }else if ("xls".equals(suffix)){
                workbook = new HSSFWorkbook(fis);
            }else {
                throw new Exception("Cannot parse this format："+suffix);
            }
            return workbook;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Apache POI - Get WorkSheet (XLS/XLSX)
     * @param workbook
     * @param sheetName
     * @return
     */
    public static Sheet getSheet(Workbook workbook, String sheetName){
        Sheet sheet = null;
        try {
            sheet = workbook.getSheet(sheetName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return sheet;
    }

    /**
     * Write Data Into Excel File
     * @param filePath
     * @param sheetName
     * @param rowNumber
     * @param columnNumber
     * @param value
     */
    public static void writeDataToFile(String filePath, String sheetName, int rowNumber,int columnNumber,String value) {
        try {
            Workbook workbook = getWorkbook(filePath);
            Sheet sheet = getSheet(workbook,sheetName);
            Row row = sheet.getRow(rowNumber);
            if (row==null){
                row = sheet.createRow(rowNumber);
            }
            if (row!=null) {
                if (row.getPhysicalNumberOfCells()==0) {
                    row = sheet.createRow(rowNumber);
                }
                Cell cell = sheet.getRow(rowNumber).getCell(columnNumber);
                if (cell==null){
                    cell = row.createCell(columnNumber);
                }
                cell.setCellType(CellType.STRING);
                cell.setBlank();
                cell.setCellValue(value);
                FileOutputStream out = new FileOutputStream(new File(filePath));
                workbook.setForceFormulaRecalculation(true);
                out.flush();
                workbook.write(out);
                out.close();
            } else {
                throw new Exception("The row you are operating not exist,Please check in output file"+row.getRowNum());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
