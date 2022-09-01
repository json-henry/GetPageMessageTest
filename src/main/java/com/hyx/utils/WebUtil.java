package com.hyx.utils;

import com.hyx.pojo.WebElement;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: HYX
 * @CreateTime: 2022-09-01  13:57
 * @Description: 页面爬取工具
 * @Version: 1.0
 */
public class WebUtil {
    /**
     * 网站的URL前缀
     */
    private static final String URL_PREFIX_ = "https://www.aquanliang.com";
    //
    /**
     * 默认网站的URL
     */
    private static final String HOME_PAGE_URL = "https://www.aquanliang.com/blog";
    /**
     * 页面总数的Html标签对应的Class
     */
    private static final String PAGE_SUM_CLASS_NAME = "_1rGJJd-K0-f7qJoR9CzyeL";
    /**
     * 类别HTML标签对应的Class
     */
    private static final String CATEGORY_CLASS_NAME = "_2LO5ZysFZxvHBuZIT1XXUu";
    /**
     * 页面信息的Class
     */
    private static final String PAGE_MESSAGE_CLASS_NAME = "_3gcd_TVhABEQqCcXHsrIpT";
    /**
     * 标题HTML标签对应的Class
     */
    private static final String TITLE_CLASS_NAME = "_3_JaaUmGUCjKZIdiLhqtfr";
    /**
     * 时间HTML标签对应的Class
     */
    private static final String TIME_CLASS_NAME = "_3TzAhzBA-XQQruZs-bwWjE";
    /**
     * 浏览量HTML标签对应的Class
     */
    private static final String VIEW_COUNT_CLASS_NAME = "_2gvAnxa4Xc7IT14d5w8MI1";


    /**
     * 存放类别以及对应的URL
     */
    public static Map<String, String> CATEGORIES_URL = new HashMap<>();

    /**
     * 获取页面的总页数
     *
     * @param url 页面的Url地址
     * @return 总页数
     */
    public static int getPageSize(String url) {
        //存放总页数,访问错误计数器
        int sum = 0, errNum = 0;
        while (sum == 0) {
            try {
                Document document = getDocument(url);
                //获取分页条元素
                Element pageBar = document.getElementsByClass(PAGE_SUM_CLASS_NAME).get(0);
                Elements liElems = pageBar.getElementsByTag("li");
                //获取总页数
                sum = Integer.parseInt(liElems.get(liElems.size() - 3).text());
            } catch (ArrayIndexOutOfBoundsException e) {
                if (++errNum >= 20) {
                    System.out.println("请检查网络连接是否正常，稍后重试！");
                    return -1;
                }
            }
        }
        return sum;
    }

    /**
     * 获取所有的类别名称
     *
     * @return 返回类别列表
     */
    public static List<String> getCategory() {
        //每次获取类别前先清空数据
        CATEGORIES_URL = new HashMap<>();
        List<String> categoryName = new ArrayList<>();
        //重试计数器,重试达到一定次数之后就退出程序，避免程序进入死循环
        int retryNum = -1;
        Elements elements = null;
        while (elements == null) {
            Document document = getDocument(HOME_PAGE_URL);
            if (document == null) {
                return null;
            }
            elements = document.getElementsByClass(CATEGORY_CLASS_NAME);
            if (++retryNum > 20) {
                System.out.println("网络异常，请稍后重试！");
                return null;
            }
        }
        Elements categoriesElements = elements.get(0).getElementsByTag("a");
        for (Element element : categoriesElements) {
            String name = element.getElementsByTag("span").text();
            String url = URL_PREFIX_ + element.attr("href") + "/";
            CATEGORIES_URL.put(name, url);
            categoryName.add(name);
        }
        return categoryName;
    }

    /**
     * 爬取页面的所有信息
     *
     * @return 返回需要爬取的所有信息
     * @throws MalformedURLException
     */
    public static List<WebElement> getWebElements(String url) throws MalformedURLException {
        //存放爬取信息的列表
        List<WebElement> elementList = new ArrayList<>(1000);

        int sum = WebUtil.getPageSize(url);

        for (int i = 1; i <= sum; i++) {
            Document page;
            Elements pageElements = null;
            int errNum = -1;
            while (pageElements == null || pageElements.size() == 0) {
                page = getDocument(url + i);
                pageElements = page.getElementsByClass(PAGE_MESSAGE_CLASS_NAME);
                if (++errNum>20){
                    return null;
                }
            }
            //获取每一个元素
            for (Element element : pageElements) {
                //封装信息实体
                WebElement webElement = new WebElement();
                //获取图片的路径
                String imageUrl = element.getElementsByTag("img").get(0).attr("src");
                webElement.setImgUrl(new URL(imageUrl));
                //获取标题
                String title = element.getElementsByClass(TITLE_CLASS_NAME).get(0).text();
                webElement.setTitle(title);
                //获取时间
                String time = element.getElementsByClass(TIME_CLASS_NAME).get(0).text();
                webElement.setDataTime(time);
                //获取浏览量
                String viewCount = element.getElementsByClass(VIEW_COUNT_CLASS_NAME).get(0).text();
                webElement.setViewCount(viewCount);
                elementList.add(webElement);
            }
        }
        return elementList;
    }

    /**
     * 将Url对应的Html信息解析为Document文件
     *
     * @param url 需要解析的Url
     * @return 解析后的Document
     */
    public static Document getDocument(String url) {
        Connection conn = Jsoup.connect(url);
        Document document = null;
        try {
            while (document==null){
                document = conn.get();
            }
        } catch (Exception e) {
            System.out.println("请检查网络连接是否正常，稍后重试！");
        }
        return document;
    }

}
