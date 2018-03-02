package org.qydata.main;

import org.apache.ibatis.session.SqlSession;
import org.qydata.tools.SqlSessionUtil;
import org.qydata.tools.WriteUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手动上传
 */
public class TestData {

    public static void main(String[] args) throws Exception {
        long l = System.currentTimeMillis();
        selectData(44);
        long l1 = System.currentTimeMillis();
        System.out.println("耗时:" + (l1 - l)/1000 + "s");
    }

    /**
     * 查询数据
     * @throws Exception
     */
    public static void selectData(int bid) throws Exception {
        SqlSession session = SqlSessionUtil.masterSqlSession();
        String statementSelect = "org.qydata.mapper.CompareDataMapper.queryData";
        int total = 0;
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= 59; i ++){
            int j = i * 10000;
            list.add(j);
        }
        Map<String, Object> map = new HashMap<>();
        List<String> dataList = new ArrayList<>();
        for ( int i = 0; i < list.size(); i ++){
            map.put("i", list.get(i));
            map.put("j", 10000);
            map.put("bid", bid);
            dataList = session.selectList(statementSelect, map);
            if (dataList != null && dataList.size() != 0 ){
                WriteUtils.writeData(dataList);
                dataList.clear();
                total ++;
            }else {
                break;
            }
        }
        System.out.println(total);
        session.close();
    }
}
