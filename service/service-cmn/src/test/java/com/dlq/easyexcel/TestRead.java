package com.dlq.easyexcel;

import com.alibaba.excel.EasyExcel;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-11 20:13
 */
public class TestRead {

    public static void main(String[] args) {

        //设置excel文件路径和文件名称
        String fileName = "D:\\Desktop\\hahaha.xlsx";

        //调用方法实现操作
        EasyExcel.read(fileName,UserData.class,new ExcelListener()).sheet().doRead();
    }
}
