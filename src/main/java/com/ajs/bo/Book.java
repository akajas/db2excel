package com.ajs.bo;

import com.ajs.piedra.excel.annotation.ExcelExport;

public class Book {

    @ExcelExport(header = "序号")
    private int id;

    @ExcelExport(header = "书名",colWidth=22)
    private String name;

    @ExcelExport(header = "作者",colWidth=18)
    private String author;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
