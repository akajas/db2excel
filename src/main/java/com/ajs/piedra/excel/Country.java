package com.ajs.piedra.excel;

import com.ajs.piedra.excel.annotation.ExcelExport;

/**
 * Created by Administrator on 2018/9/12 0012.
 */
public class Country {

    @ExcelExport(header = "国家")
    private String country;

    @ExcelExport(header = "id")
    private int id;

    public Country() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Country( int id,String country) {
        this.country = country;
        this.id = id;
    }
}
