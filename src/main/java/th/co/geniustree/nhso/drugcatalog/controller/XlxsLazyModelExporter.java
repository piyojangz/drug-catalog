/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package th.co.geniustree.nhso.drugcatalog.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author moth
 */
public class XlxsLazyModelExporter {
    public static void export(LazyDataModel model,String sheetName,FileOutputStream out) throws IOException{
        Workbook wb = new XSSFWorkbook();
        if(sheetName==null){
            sheetName="Sheet1";
        }
        Sheet sheet1 = wb.createSheet(sheetName);
        
        wb.write(out);
    }
}
