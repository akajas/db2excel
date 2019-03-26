package com.ajs.piedra.excel;

import com.ajs.piedra.excel.util.ExcelExportor;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/12 0012.
 */
public class ExcelTest1 {

    @Test
    public void tt(){
        OutputStream out = null;
        try {
            out = new FileOutputStream(new File("D://EXCEL-EXPORT-TEST2.xls"));

            Map<String, List<Country>> mapList = new LinkedHashMap<String, List<Country>>();


            List<Country> stus = new ArrayList<Country>();
            {
                stus.add(new Country(1, "China1"));
                stus.add(new Country(2,"USA1"));
                stus.add(new Country(3,"JPN1"));
                stus.add(new Country(4,"法国1"));
            }
            List<Country> stus2 = new ArrayList<Country>();
            {
                stus2.add(new Country(1, "China2"));
                stus2.add(new Country(2,"USA2"));
                stus2.add(new Country(3,"JPN2"));
                stus2.add(new Country(4,"法国2"));
            }
            mapList.put("表头1", stus);
            mapList.put("表头2", stus2);

            new ExcelExportor<Country>().exportExcelMulSheet(mapList, out);
//            for(){
//                stus.add(new ExcelRow());
//            }

//            new ExcelExportor<Country>().exportExcel("测试单表头2",  stus2, out);

            System.out.println("excel导出成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    //Ignore..
                } finally{
                    out = null;
                }
            }
        }
    }
}
