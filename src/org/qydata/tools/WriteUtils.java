package org.qydata.tools;

import org.qydata.entity.Data;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WriteUtils {
    /**
     * 加密
     * @param list
     * @throws Exception
     */
    public static void writeData(List<String>list) throws Exception {
        String tomorrow = DateUtils.tomorrow();
//        int i = 1;
        BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\dell\\Desktop\\data_" + tomorrow + ".txt"),true));
//        BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\dell\\Desktop\\data_" + i + ".txt"),true));
        if (list != null && list.size() != 0) {
            for (String s : list) {
                //将数据加密
                String s2 = EncrUtil.encrypt(s);
                byte[] bytes2 = s2.getBytes();
                out.write(bytes2);
//                out.write(s.getBytes());
                out.write("\r\n".getBytes());
            }
        }
        out.flush();
        out.close();;
    }

    /**
     * 解密
     * @param str
     * @throws Exception
     */
    public static void writeKeyData(String str) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\dell\\Desktop\\test-taiyuenimeide" + format + ".txt"),true));
        byte[] bytes = str.getBytes();
        out.write(bytes);
        out.write("\r\n".getBytes());

        out.flush();
        out.close();;
    }


}
