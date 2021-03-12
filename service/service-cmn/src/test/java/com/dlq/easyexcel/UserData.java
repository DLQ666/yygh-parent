package com.dlq.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-11 20:04
 */
@Data
public class UserData {

    @ExcelProperty(value = "用户编号",index = 0)
    private int uid;

    @ExcelProperty(value = "用户名称",index = 1)
    private String username;
}
