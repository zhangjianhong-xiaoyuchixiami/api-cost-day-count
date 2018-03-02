package org.qydata.tools;

import java.math.BigInteger;
/**
* 可逆的加密方法工具类
*/
public class EncrUtil {
    private static final int RADIX = 36;
    private static final String pwd = "99026156";
    /**
     * 
    * @Title: encrypt
    * @Description: 加密方法 
    * author:Li YunLong 
    * @param  str 待加密字符串
    * @return String
    * @date 2018-1-18
    * @throws
     */
    public static final String encrypt(String str) {
        if (str == null||str.length() == 0) return "";
        BigInteger bi_passwd = new BigInteger(str.getBytes());
        BigInteger bi = new BigInteger(pwd,RADIX);
        bi = bi.xor(bi_passwd);
        return bi.toString(RADIX).toUpperCase();
    }
    /**
     * 
    * @Title: decrypt
    * @Description: 解密方法 
    * author:Li YunLong 
    * @param  encrypted 待解密字符串
    * @return String
    * @date 2018-1-18
    * @throws
     */
    public static final String decrypt(String encrypted) {
        if (encrypted == null||encrypted.length() == 0) return "";
        BigInteger bi_confuse = new BigInteger(pwd,RADIX);
        try {
            BigInteger bi = new BigInteger(encrypted.toLowerCase(), RADIX);
            bi = bi.xor(bi_confuse);
            return new String(bi.toByteArray());
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        String encrypt = encrypt("17316206369");
        System.out.println(encrypt);
        String decrypt = decrypt("7H4QWZV76A201SBQR");
        System.out.println(decrypt);
    }
}