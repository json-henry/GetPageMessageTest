package com.hyx.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.net.URL;

/**
 * @Author: HYX
 * @CreateTime: 2022-09-01  11:38
 * @Description: 封装记录实体
 * @Version: 1.0
 */
@Data
@Accessors(chain = true)
@ContentRowHeight(135)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,verticalAlignment = VerticalAlignmentEnum.CENTER)//表头样式
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,verticalAlignment = VerticalAlignmentEnum.CENTER)//内容样式
public class WebElement {
    @ExcelProperty(value = "封面",index = 0)
    @ColumnWidth(40)
    private URL imgUrl;
    @ExcelProperty(value = "标题",index = 1)
    @ColumnWidth(110)
    private String title;
    @ExcelProperty(value = "发布日期",index = 2)
    @ColumnWidth(30)
    private String dataTime;
    @ExcelProperty(value = "阅读数",index = 3)
    @ColumnWidth(30)
    private String viewCount;
}
