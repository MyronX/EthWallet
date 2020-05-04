package utils;

import lombok.Data;

/**
 * 用来存放解析出来的KeyStore文件内容
 */
@Data
public class KeyStore {
    /**
     * 地址
     */
    String address;
    /**
     * 私钥
     */
    String privateKey;
    /**
     * 公钥
     */
    String publicKey;

}
