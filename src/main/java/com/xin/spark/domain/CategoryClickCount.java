package com.xin.spark.domain;

/**
 * Created by xinBa.
 * User: 辛聪明
 * Date: 2020/4/5
 * 类别访问数量实体类
 */
public class CategoryClickCount {
    private String categoryName;
    private long value;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CategoryClickCount{" +
                "categoryName='" + categoryName + '\'' +
                ", value=" + value +
                '}';
    }
}
