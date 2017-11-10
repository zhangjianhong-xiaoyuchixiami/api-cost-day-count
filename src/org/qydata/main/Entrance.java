package org.qydata.main;

import org.apache.ibatis.session.SqlSession;
import org.qydata.po.ApiCost;
import org.qydata.tools.CalendarTools;
import org.qydata.tools.SqlSessionUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jonhn on 2017/4/19.
 */
public class Entrance {

    public static void delete(){
        SqlSession session = SqlSessionUtil.masterSqlSession();
        Map<String,Object> mapDelete = new HashMap<>();
        mapDelete.put("consuTime", CalendarTools.getCurrentPreviousTime());
        String statementDelete = "org.qydata.mapper.ApiCostMapper.deleteApiConsume";
        session.delete(statementDelete,mapDelete);
        session.commit();
        session.close();
    }

    public static List<ApiCost> query(){
        SqlSession session = SqlSessionUtil.slaveSqlSession();
        String statementSelect = "org.qydata.mapper.ApiCostMapper.queryApiConsume";
        Map<String, Object> mapSelect = new HashMap<>();
        mapSelect.put("beginDate", CalendarTools.getCurrentPreviousTime() + " 00:00:00");
        mapSelect.put("endDate",CalendarTools.getCurrentTime());
        System.out.println(mapSelect.get("beginDate"));
        System.out.println(mapSelect.get("endDate"));
        List<ApiCost> apiCostList = session.selectList(statementSelect, mapSelect);
        session.close();
        return apiCostList;
    }

    public static void insert(List<ApiCost> list) throws ParseException {
        SqlSession session = SqlSessionUtil.masterSqlSession();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (list == null || list.size() <= 0){
            return;
        }
        for (ApiCost api : list) {
            api.setApiId(api.getApiId());
            api.setYears(api.getYears());
            api.setMonths(api.getMonths());
            api.setDays(api.getDays());
            api.setTotleCost(api.getTotleCost());
            api.setUsageAmount(api.getUsageAmount());
            api.setFeeAmount(api.getFeeAmount());
            api.setConsuTime(sdf.parse(api.getYears()+"/"+api.getMonths()+"/"+api.getDays()));
        }
        String statementInsert = "org.qydata.mapper.ApiCostMapper.insertApiConsume";
        session.insert(statementInsert, list);
        session.commit();
        session.close();
    }

    public static void main(String[] args) throws ParseException {
        Entrance.delete();
        Entrance.insert(Entrance.query());
    }

}
