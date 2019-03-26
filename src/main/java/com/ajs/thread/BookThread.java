package com.ajs.thread;

import com.ajs.bo.Book;
import com.ajs.piedra.excel.util.ExcelExportor;
import com.ajs.service.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
public class BookThread implements Runnable {

    @Value("${dir.mainDirectory}")
    private String mainDirectory;

    @Resource
    private BookService bookService;


    @Override
    public void run() {

        findAllBook();

    }

    /**
     * 查询所有图书
     */
    private void findAllBook(){
        //1. 获取数据
        List<Book> bookList=bookService.findAllBook();

        //2. 创建文件夹
        File files = new File(mainDirectory);
        if(!files.exists()){// 判断文件夹是否存在，如果不存在就创建一个文件夹
            files.mkdirs();
        }

        //3. 创建xls文件
        OutputStream out = null;
        try {

            out = new FileOutputStream(new File(mainDirectory + "图书表.xls"));

            //Sheet名
            String title="图书表";

            new ExcelExportor<Book>().exportExcel(title,bookList, out);

            System.out.println("图书表excel导出成功！");

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
