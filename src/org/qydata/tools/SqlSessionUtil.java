package org.qydata.tools;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.qydata.main.Entrance;

import java.io.InputStream;

/**
 * Created by jonhn on 2017/10/20.
 */
public class SqlSessionUtil {

    public static SqlSession masterSqlSession(){
        //String resource_master = "mybatis_master_test.xml";
        String resource_master = "mybatis_master.xml";
        InputStream is = Entrance.class.getClassLoader().getResourceAsStream(resource_master);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        return session;
    }

    public static SqlSession slaveSqlSession(){
        //String resource_slave = "mybatis_slave_test.xml";
        String resource_slave = "mybatis_slave.xml";
        InputStream is = Entrance.class.getClassLoader().getResourceAsStream(resource_slave);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        return session;
    }

}
