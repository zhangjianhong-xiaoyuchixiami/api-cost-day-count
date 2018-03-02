package org.qydata.tools;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.qydata.entity.Ftp;

import java.io.*;

public class FtpUtils {

    private static FTPClient ftp;
    /**
     * 获取ftp连接
     * @param f
     * @return@throws Exception
     */
    public static boolean connectFtp(Ftp f) throws Exception{
        ftp=new FTPClient();
        boolean flag=false;
        int reply;
        if (f.getPort()==null) {
            ftp.connect(f.getIpAddr(),21);
        }else{
            ftp.connect(f.getIpAddr(),f.getPort());
        }
        ftp.login(f.getUserName(), f.getPwd());
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return flag;
        }
        //线上
        boolean b = ftp.changeWorkingDirectory("result/");
        //测试
//        boolean b1 = ftp.changeWorkingDirectory("qydata/");
        System.out.println(b);
        flag = true;
        return flag;
    }
    /**
     * 关闭ftp连接
     */
    public static void closeFtp(){
        if (ftp!=null && ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * ftp上传文件
     * @param f
     * @throws Exception
     */
    public static void upload(File f) throws Exception{
        if (f.isDirectory()) {
            ftp.setControlEncoding("UTF-8");
            ftp.makeDirectory(f.getName());
            //线上
            boolean b = ftp.changeWorkingDirectory("result/");
            //测试
//            boolean b1 = ftp.changeWorkingDirectory("qydata/");
            System.out.println(b);
            String[] files=f.list();
            for(String fstr : files){
                File file1=new File(f.getPath()+"/"+fstr);
                if (file1.isDirectory()) {
                    upload(file1);
                    ftp.changeToParentDirectory();
                }else{
                    File file2=new File(f.getPath()+"/"+fstr);
                    FileInputStream input=new FileInputStream(file2);
                    ftp.storeFile(file2.getName(),input);
                    input.close();
                }
            }
        }else{
            File file2=new File(f.getPath());
            FileInputStream input=new FileInputStream(file2);
            ftp.storeFile(file2.getName(),input);
            input.close();
        }
    }

    public static void main(String[] args) throws Exception{
        Ftp f = new Ftp();
        f.setIpAddr("36.110.85.10");
        f.setUserName("qianyan");
        f.setPwd("9Vbvzm6D3Sdx");
        boolean b = FtpUtils.connectFtp(f);
        System.out.println(b);
        File file = new File("C:\\Users\\dell\\Desktop\\data_20180124.txt");
        FtpUtils.upload(file);//把文件上传在ftp上
        System.out.println("ok");
    }
}
