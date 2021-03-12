package com.dlq.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-11 20:06
 */
public class TestWrite {

    public static void main(String[] args) {

        //设置excel文件路径和文件名称
        String fileName = "D:\\Desktop\\hahaha.xlsx";

        //构建数据list集合
        ArrayList<UserData> userData = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            UserData data = new UserData();
            data.setUid(i);
            data.setUsername("hahaha"+i);
            userData.add(data);
        }

        //调用方法实现写操作
        EasyExcel.write(fileName,UserData.class).sheet("用户信息").doWrite(userData);
    }
}
