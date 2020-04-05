package com.xin.spark.controller;

import com.xin.spark.dao.CategoryClickCountDAO;
import com.xin.spark.domain.CategoryClickCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinBa.
 * User: 辛聪明
 * Date: 2020/4/5
 * 视频类目访问量实时查询展示功能实现以及扩展
 */
@RestController
public class SparkStatAPP {

    private static Map<String,String> courses = new HashMap<>();
    static {
        courses.put("1","偶像爱情");
        courses.put("2","宫斗谋权");
        courses.put("3","玄幻史诗");
        courses.put("4", "都市生活");
        courses.put("5", "罪案谍战");
        courses.put("6", "历险科幻");
    }

    @Autowired
    CategoryClickCountDAO courseClickCountDAO;

    @RequestMapping(value = "/CategoryClickCount", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryClickCount> courseClickCount() throws Exception {
        List<CategoryClickCount> list = courseClickCountDAO.query("20200405");
        for(CategoryClickCount model:list){
            String s = courses.get(model.getCategoryName().substring(9));
            if (s!=null){
                model.setCategoryName(s);
            }else {
                model.setCategoryName("其他");
            }
        }
        System.out.println("list:"+list);
        return list;
    }
    @RequestMapping(value = "/echarts", method = RequestMethod.GET)
    public ModelAndView echarts(){
        return new ModelAndView("echarts");
    }
}
