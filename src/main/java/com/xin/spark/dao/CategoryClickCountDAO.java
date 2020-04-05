package com.xin.spark.dao;

import com.xin.spark.domain.CategoryClickCount;
import com.xin.spark.utils.HBaseUtils;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xinBa.
 * User: 辛聪明
 * Date: 2020/4/5
 */
@Repository
public class CategoryClickCountDAO {
    public List<CategoryClickCount> query(String day) throws IOException {
        List<CategoryClickCount> list = new ArrayList<>();
        Map<String,Long> map = HBaseUtils.getInstance().query("category_clickcount",day);
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            CategoryClickCount categoryClickCount = new CategoryClickCount();
            categoryClickCount.setCategoryName(entry.getKey());;
            categoryClickCount.setValue(entry.getValue());
            list.add(categoryClickCount);
        }
        return list;
    }
    public static void main(String[] args) throws IOException {
        CategoryClickCountDAO dao = new CategoryClickCountDAO();
        List<CategoryClickCount> list = dao.query("20200404");
        for (CategoryClickCount c : list) {
            System.out.println(c.getValue());
        }
    }

}
