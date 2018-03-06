package org.qydata.main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.qydata.entity.Data;
import org.qydata.entity.Ftp;
import org.qydata.tools.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


public class Compare {

    private static String [] address  = {"liqing@qianyandata.com"};
    public static void main(String[] args) throws Exception {
//        int a = 1;
        try {

            String customer = "212";
            insertBid(customer);
            int bid = selectBid();
            Date deat1 = new Date();
            long l1 = System.currentTimeMillis();
            //查询插入数据

            selectAndInsertData(bid, customer);
            long l2 = System.currentTimeMillis();
            //读数据
            readData(bid);
            long l3 = System.currentTimeMillis();
            Date deat2 = new Date();
            System.out.println("查询插入的时间:" + (l2 - l1)/1000 + "s");
            System.out.println("加密输出的事件:" + (l3 - l2)/1000 + "s");

            //把文件上传在ftp上
            //线上
            Ftp f = new Ftp();
            f.setIpAddr("36.110.85.10");
            f.setUserName("qianyan");
            f.setPwd("9Vbvzm6D3Sdx");

            boolean b = FtpUtils.connectFtp(f);
            System.out.println(b);
            File file = new File("C:\\Users\\dell\\Desktop\\data_" + DateUtils.tomorrow() +".txt");
            FtpUtils.upload(file);

            //测试
            //把文件上传在ftp上
           /* Ftp f = new Ftp();
            f.setIpAddr("192.168.111.148");
            f.setUserName("weilai");
            f.setPwd("doiiKk5u51q");

            String tomorrow = DateUtils.tomorrow();
            boolean b = FtpUtils.connectFtp(f);
            System.out.println(b);
            File file = new File("C:\\Users\\dell\\Desktop\\data_" + a + ".txt");
            FtpUtils.upload(file);*/
            SendEmail.sendMail(address,"泰岳ftp数据正常执行",
                "启动时间：" +
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(deat1) +
                    "，结束时间：" +
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(deat2) +
                    "，读取插入运行时长：" + (l2 - l1) + "ms," + "写数据时长" + (l3 - l2) +
                    "ms");
        } catch (Exception e) {
            SendEmail.sendMail(address,"泰岳ftp数据异常",e.getMessage());
//            e.printStackTrace();
        }

    }

    public static int insertData(List<Data>list){
        SqlSession session = SqlSessionUtil.masterSqlSession();
        String statementInsert = "org.qydata.mapper.CompareDataMapper.insertData";
        int i = session.insert(statementInsert, list);
        session.commit();
        session.close();
        return i;
    }

    public static void selectAndInsertData(int bid, String customerId) throws Exception {
        SqlSession session = SqlSessionUtil.slaveSqlSession();
        //取60W数据的session
//        String statementSelect = "org.qydata.mapper.CompareDataMapper.compareData";
//      按customerId取数据
        String statementSelect = "org.qydata.mapper.CompareDataMapper.getMobile";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        int count = 0;
        int i = 0;
        int n = 0;
        int j = 0;
        Map<String, Object>map = new HashMap<>();
//        Map<String, Object>map1 = new HashMap<>();
        List<Data> dataList = null;
        while ( true ){
            map.put("i", i * 10000);
            map.put("j", 10000);
            map.put("beginDate", DateUtils.afterYesterdayDawn());
//            map.put("endDate", DateUtils.yesterdayDawn());
//            map.put("beginDate", DateUtils.yesterdayDawn());
            map.put("endDate", DateUtils.todayDawn());
            map.put("customerId", customerId);

            System.out.println("从第" + map.get("i") + "条开始");
            long l2 = System.currentTimeMillis();
            dataList = session.selectList(statementSelect, map);
            long l3 = System.currentTimeMillis();
            System.out.println("查询时间:" + (l3 - l2) / 1000 + "s");

            /*if (dataList ==null || dataList.size() == 0){
                //查不到了需要切换还数据源  新数据源 还是从头开始取数据

                System.out.println("切换数据源了");
                map1.put("i", j * 10000);
                map1.put("j", 10000);

                SqlSession anotherSession = SqlSessionUtil.anotherSqlSession();
                dataList = anotherSession.selectList(statementSelect, map1);

//                if (dataList == null || dataList.size() == 0){
//                    new Exception("没数据了");
//                }
                //切换数据源
                if (dataList != null && dataList.size() > 0) {
                    List<Data> tempList = new ArrayList<>();
                    for (Data data : dataList) {
                        if (data != null) {
                            if (data.getContent() != null) {
                                JSONObject jsonObject = JSON.parseObject(data.getContent());
                                String mobile = jsonObject.getString("mobile");
                                //判断content总是否有mobile
                                if (mobile != null && mobile.length() > 0) {
                                    if (OperatorList.isOperator(mobile) == 0) {
                                        data.setDate(sdf.format(new Date()));
                                        data.setBid(bid);
                                        data.setMobile(mobile);
                                        data.setContent(null);
                                        tempList.add(data);
                                    }
                                }
                            }
                        }
                    }
                    if (tempList != null && tempList.size() > 0) {
                        long l = System.currentTimeMillis();
                        int res = insertData(tempList);
                        long l1 = System.currentTimeMillis();
                        System.out.println("插入的时间为:" + (l1 - l) / 1000 + "s");
                        //插入0条数据就往后跳10万
                        if (res == 0) {
                            int j1 = selectJ();
                            j = j + j1;
                        }
                        System.out.println("此次插入数:" + res);
                        count += res;
                        System.out.println("总数量:" + count);
                        if (count - 600000 > 0) {
                            System.out.println("更新i和j");
                            updateI(i);
                            updateJ(j);
                            break;
                        }
                        dataList.clear();
                        tempList.clear();
                    }
                    System.out.println("j = " + j);
                    j++;
                }
            }*/
            if (dataList != null && dataList.size() > 0) {

                List<Data> tempList = new ArrayList<>();
                for (Data data : dataList) {
                    if (data != null) {
                        if (data.getContent() != null) {
                            JSONObject jsonObject = JSON.parseObject(data.getContent());
                            String mobile = jsonObject.getString("mobile");
                            //判断content总是否有mobile
                            if (mobile != null && mobile.length() > 0) {
                                if (OperatorList.isOperator(mobile) == 0) {
                                    data.setDate(sdf.format(new Date()));
                                    data.setBid(bid);
                                    data.setMobile(mobile);
                                    data.setContent(null);
                                    tempList.add(data);
                                }
                            }
                        }
                    }
                }
                if (tempList != null && tempList.size() > 0) {
                    long l = System.currentTimeMillis();
                    int res = insertData(tempList);
                    long l1 = System.currentTimeMillis();
                    System.out.println("插入的时间为:" + (l1 - l) / 1000 + "s");
                   /* //插入0条数据就往后跳10万
                    if (res == 0) {
                        System.out.println("小心点!!!!!");
                        int i1 = selectI();
                        i = i + i1;
                    }*/
                    System.out.println("此次插入数:" + res);
                    count += res;
                    System.out.println("总数量:" + count);
                   /* if (count - 600000 > 0) {
                        System.out.println("更新i");
                        updateI(i);
                        break;
                    }*/
                    dataList.clear();
                    tempList.clear();
                }
                System.out.println("i = " + i);
                i++;
            }else{
                break;
            }
        }
        session.close();
    }

    public static void readData(int bid) throws Exception {
        SqlSession session = SqlSessionUtil.masterSqlSession();
        String statementSelect1 = "org.qydata.mapper.CompareDataMapper.readData";
        int count = 0;
        int total = 0;
        List<Integer> list = new ArrayList<Integer>();
        int selectBid = selectBid();
        int countData = countData(selectBid);
        int countd = (int)Math.ceil(countData/10000);
        System.out.println("总数:" + countd);
        for (int i = 0; i <= countd; i ++){
            int j = i * 10000;
            list.add(j);
        }
        Map<String, Object>map = new HashMap<>();
        List<String> dataList = new ArrayList<>();
        for ( int i = 0; i < list.size(); i ++){
            map.put("i", list.get(i));
            map.put("j", 10000);
            map.put("bid", bid);
            dataList = session.selectList(statementSelect1, map);
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

    public static int selectBid(){
        SqlSession session = SqlSessionUtil.masterSqlSession();
        String statementSelect = "org.qydata.mapper.CompareDataMapper.selectBid";
        List<Object> list = session.selectList(statementSelect);
        Object o = list.get(0);
        int bid = Integer.valueOf(o.toString());
        session.close();
        return bid;
    }

    public static void insertBid(String customerId){
        SqlSession session = SqlSessionUtil.masterSqlSession();
        String statementInsert = "org.qydata.mapper.CompareDataMapper.insertBid";
        String s = "data_" + DateUtils.tomorrow() + "-" + customerId;
        int i = session.insert(statementInsert, s);
        session.commit();
        session.close();
    }

    public static int selectI(){
        SqlSession sqlSession = SqlSessionUtil.countSqlSession();
        String statementSelectI = "org.qydata.mapper.CompareDataMapper.queryI";
        List<Object> objects = sqlSession.selectList(statementSelectI, 1);
        Integer i = Integer.valueOf(objects.get(0).toString());
        sqlSession.close();
        return i;
    }

    public static int selectJ(){
        SqlSession sqlSession = SqlSessionUtil.countSqlSession();
        String statementSelectI = "org.qydata.mapper.CompareDataMapper.queryJ";
        List<Object> objects = sqlSession.selectList(statementSelectI, 1);
        Integer j = Integer.valueOf(objects.get(0).toString());
        sqlSession.close();
        return j;
    }

    public static void updateI(int i){
        SqlSession sqlSession = SqlSessionUtil.countSqlSession();
        String statementUpdateI = "org.qydata.mapper.CompareDataMapper.setCounti";
        sqlSession.update(statementUpdateI, i);
        sqlSession.commit();
        sqlSession.close();
    }

    public static void updateJ(int j){
        SqlSession sqlSession = SqlSessionUtil.countSqlSession();
        String statementUpdateI = "org.qydata.mapper.CompareDataMapper.setCountj";
        sqlSession.update(statementUpdateI, j);
        sqlSession.commit();
        sqlSession.close();
    }


    public static int countData(int bid){
        SqlSession session = SqlSessionUtil.masterSqlSession();
        String statementSelect = "org.qydata.mapper.CompareDataMapper.countData";
        Object o = session.selectOne(statementSelect, bid);
        int i = Integer.valueOf(o.toString());
        return i;
    }

}

