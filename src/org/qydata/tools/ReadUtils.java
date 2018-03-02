package org.qydata.tools;

import java.io.*;
import java.nio.file.Files;

/**
 * 测试加密解密
 */
public class ReadUtils {

    public static void readData() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dell\\Desktop\\test-taiyue2018-01-18.txt"));
        String str = null;
        while((str = br.readLine()) != null){
            String s = EncrUtil.decrypt(str);
            WriteUtils.writeKeyData(s);
        }
        br.close();
    }

    public static void main(String[] args) throws Exception {
        readData();
    }
}
