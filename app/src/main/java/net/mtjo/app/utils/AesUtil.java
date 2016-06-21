package net.mtjo.app.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * <h3>AesUtil</h3>
 * 
 * <p>
 * 
 * </p>
 * 
 * @company 广州快律网络科技有限公司
 * @copyright CopyRight (c) 2012
 * 
 * @author chenjinlian
 * @date 2014年2月19日
 * @version 1.0
 */
public class AesUtil
{
	private static String key = "^123@Bcde33#4I4$";
	
	public static String encrypt(String sSrc) throws Exception{
		return encrypt(sSrc,key);
	}

	/** 
     * 加密--把加密后的byte数组先进行二进制转16进制在进行base64编码 
     * 
     * @param sSrc 
     * @param sKey 
     * @return 
     * @throws Exception 
     */  
    public static String encrypt(String sSrc, String sKey) throws Exception {  
        if (sKey == null) {  
            throw new IllegalArgumentException("Argument sKey is null.");  
        }  
        if (sKey.length() != 16) {  
            throw new IllegalArgumentException(  
                    "Argument sKey'length is not 16.");  
        }  

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");    
                int blockSize = cipher.getBlockSize();

        byte[] dataBytes = sSrc.getBytes("UTF-8");
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }

        byte[] plaintext = new byte[plaintextLength];
        
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
         
        SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes("ASCII"), "AES");
     
        cipher.init(Cipher.ENCRYPT_MODE, keyspec);  
       
        byte[] encrypted = cipher.doFinal(plaintext);
        
        String tempStr = parseByte2HexStr(encrypted);  
  
        
        return tempStr;  
    }  
  
    /** 
     *解密--先 进行base64解码，在进行16进制转为2进制然后再解码 密码16位
     *
     * @param sSrc 
     * @param sKey 
     * @return 
     * @throws Exception 
     */  
    public static String decrypt(String sSrc, String sKey) throws Exception {  
  
        if (sKey == null) {  
            throw new IllegalArgumentException("499");  
        }  
        if (sKey.length() != 16) {  
            throw new IllegalArgumentException("498");  
        }  
  
        byte[] raw = sKey.getBytes("ASCII");  
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
  
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");  
       
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);  
  
        byte[] encrypted1 = sSrc.getBytes();  
  
        String tempStr = new String(encrypted1, "utf-8");  
        encrypted1 = parseHexStr2Byte(tempStr);  
        byte[] original = cipher.doFinal(encrypted1);  
        String originalString = new String(original, "utf-8");  
        if(originalString != null){
        	originalString = originalString.trim();
        }
        return originalString;  
    }  
  
    /** 
     * 将二进制转换成16进制 
     *  
     * @param buf 
     * @return 
     */  
    public static String parseByte2HexStr(byte buf[]) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) {  
            String hex = Integer.toHexString(buf[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            sb.append(hex.toUpperCase());  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 将16进制转换为二进制 
     *  
     * @param hexStr 
     * @return 
     */  
    public static byte[] parseHexStr2Byte(String hexStr) {  
        if (hexStr.length() < 1)  
            return null;  
        byte[] result = new byte[hexStr.length() / 2];  
        for (int i = 0; i < hexStr.length() / 2; i++) {  
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);  
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),  
                    16);  
            result[i] = (byte) (high * 16 + low);  
        }  
        return result;  
    }  
}
