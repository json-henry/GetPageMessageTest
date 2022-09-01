package com.hyx;

import cn.hutool.core.collection.CollectionUtil;
import com.hyx.pojo.WebElement;
import com.hyx.utils.ExcelUtil;
import com.hyx.utils.WebUtil;

import java.net.MalformedURLException;
import java.util.*;

import static com.hyx.utils.WebUtil.getWebElements;


/**
 * @Author: HYX
 * @CreateTime: 2022-09-01  11:08
 * @Description: 应用程序
 * @Version: 1.0
 */
public class Application {

    public static void main(String[] args) throws MalformedURLException {
        Scanner scanner;
        List<String> category = WebUtil.getCategory();
        if (CollectionUtil.isEmpty(category)) {
            System.out.println("获取栏目失败，请查看网络连接是否正常后稍后重试！");
            return;
        }
        System.out.println("请输入对应序号导出该栏目下对应的所有信息(输入整数0结束程序):");
        int num = 0;

        for (String s : category) {
            System.out.print(++num + s + "\t");
        }
        System.out.println();
        int input;
        while (true) {
            scanner = new Scanner(System.in);
            try {
                input = scanner.nextInt();
                if (input > num || input < 0) {
                    throw new InputMismatchException();
                } else if (input == 0) {
                    System.out.println("退出程序...");
                    return;
                }
                //获取栏目对应的URL
                String url = WebUtil.CATEGORIES_URL.get(category.get(input - 1));
                System.out.println("正在获取《"+category.get(input-1)+"》栏下的所有内容...");
                //通过URL爬取页面的信息
                List<WebElement> webElements = getWebElements(url);
                if (CollectionUtil.isEmpty(webElements)){
                    System.out.println("获取失败，请检查网络是否正常后重试！");
                    return;
                }
                System.out.println("获取数据成功，正在导出到Excel文件中...");
                ExcelUtil.writeToExcel(".\\圈量科技" + category.get(input - 1) + "数据.xlsX", webElements);
                System.out.println("导出数据成功！");
                break;
            } catch (InputMismatchException e) {
                System.out.println("请重新输入0~6的整数!");
            }
        }
    }

}
