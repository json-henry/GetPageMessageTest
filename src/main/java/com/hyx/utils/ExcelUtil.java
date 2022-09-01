package com.hyx.utils;


import com.alibaba.excel.EasyExcel;
import com.hyx.pojo.WebElement;

import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-09-01  11:03
 * @Description: Excel工具类
 * @Version: 1.0
 */
public class ExcelUtil {

    public static void writeToExcel(String fileName, List<WebElement> data) {
        EasyExcel.write(fileName,WebElement.class).sheet().doWrite(data);
    }


}
