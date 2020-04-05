package com.xin.spark.utils;

import com.xin.spark.domain.CategoryClickCount;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * HBase 操作工具类
 */
public class HBaseUtils {
    private HBaseAdmin admin = null;
    private Configuration configration = null;
    /**
     * 私有构造方法
     */
    private HBaseUtils(){
        configration = new Configuration();
        configration.set("hbase.zookeeper.quorum", "hdp-1:2181");
        configration.set("hbase.rootdir", "hdfs://hdp-1/hbase");
        try {
            admin = new HBaseAdmin(configration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HBaseUtils instance = null;
    /**
     * 获取单实例对象
     * @return
     */
    public static synchronized HBaseUtils getInstance(){
        if(null == instance){
            instance = new HBaseUtils();
        }
        return instance;
    }
    /**
     * 根据表明获取到 Htable 实例
     * @param tableName
     * @return
     */
    public HTable getTable(String tableName){
        HTable table = null;
        try {
            table = new HTable(configration,tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    /**
     * 添加一条记录到 Hbase 表 70 30 128 32 核 200T 8000
     *
     * @param tableName Hbase 表名
     * @param rowkey Hbase 表的 rowkey
     * @param cf Hbase 表的 columnfamily
     * @param column Hbase 表的列
     * @param value 写入 Hbase 表的值
     */
    public void put(String tableName,String rowkey,String cf,String column,String value){
        HTable table = getTable(tableName);
        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(cf),Bytes.toBytes(column),Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据表名输入条件获取 Hbase 的记录数
     */
    public Map<String, Long> query(String tableName, String condition) throws IOException {
        Map<String, Long> map = new HashMap<>();
        HTable table = getTable(tableName);
        String cf = "info";
        String qualifier = "click_count";
//      创建扫描仪
        Scan scan = new Scan();
//      创建前缀过滤器
        Filter filter = new PrefixFilter(Bytes.toBytes(condition));
        scan.setFilter(filter);

        ResultScanner rs = table.getScanner(scan);
        for (Result result : rs) {
//          得到rowkey
            String row = Bytes.toString(result.getRow());
            //Byte直接转long，先转string在转long会报错
            long clickCount = Bytes.toLong(result.getValue(cf.getBytes(),
                    qualifier.getBytes()));

            map.put(row, clickCount);
        }
        return map;
    }

    public void getOneDataByRowKey(String tableName,String rowkey)throws Exception{
        HTable table = getTable(tableName);
        Get g=new Get(Bytes.toBytes(rowkey));
        Result r=table.get(g);
        for(KeyValue k:r.raw()){
            System.out.println("行号: "+Bytes.toStringBinary(k.getRow()));
            System.out.println("时间戳: "+k.getTimestamp());
            System.out.println("列簇: "+Bytes.toStringBinary(k.getFamily()));
            System.out.println("列: "+Bytes.toStringBinary(k.getQualifier()));
//if(Bytes.toStringBinary(k.getQualifier()).equals("myage")){
// System.out.println("值: "+Bytes.toInt(k.getValue()));
//}else{
            long ss= Bytes.toLong(k.getValue());
            System.out.println("值: "+ss);
//}
        }
        table.close();
    }

    public static void main(String[] args) throws Exception {
        Map<String, Long> map = HBaseUtils.getInstance().query("category_clickcount",
                "20200404");
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
//        HBaseUtils.getInstance().getOneDataByRowKey("category_clickcount","20200404_1");
    }


}