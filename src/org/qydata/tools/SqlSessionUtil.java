package org.qydata.tools;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.qydata.main.Compare;

import java.io.InputStream;


public class SqlSessionUtil {

    public static SqlSession masterSqlSession(){
        String resource_master = "mybatis_master.xml";
        InputStream is = Compare.class.getClassLoader().getResourceAsStream(resource_master);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        return session;
    }

    public static SqlSession slaveSqlSession(){
        String resource_slave = "mybatis_slave.xml";
        InputStream is = Compare.class.getClassLoader().getResourceAsStream(resource_slave);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        return session;
    }

    public static SqlSession anotherSqlSession(){
        String resource_another = "mybatis_another.xml";
        InputStream is = Compare.class.getClassLoader().getResourceAsStream(resource_another);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        return session;
    }

    public static SqlSession countSqlSession(){
        String resource_count = "mybatis_count.xml";
        InputStream is = Compare.class.getClassLoader().getResourceAsStream(resource_count);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        return session;
    }

}
