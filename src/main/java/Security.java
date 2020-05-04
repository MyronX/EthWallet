import config.Config;
import org.web3j.crypto.*;
import utils.KeyStore;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/**
 * keystore 管理相关
 * 存在的问题 导入的文件后会生成json文件，但是解析却只能解析原生的不带.json的文件后缀
 * @Author MyronX
 */
public class Security {

    /**
     * 通过密码和keystore文件解析出对应的地址和私钥和公钥
     * @param password 账户对应的密码
     * @param keystorePath 存放keystore文件的路径，要加上文件名
     * @return 返回的是一个keystore对象 保存者这个文件对应的地址和密钥对
     */
    private static KeyStore parseKeyFile(String password, String keystorePath) {

        //创建一个用于存放解析后的数据的对象
        KeyStore keyStore = new KeyStore();

        Credentials credentials = null;
        try {
            //根据文件和密码创建一个证书
            credentials = WalletUtils.loadCredentials(password, keystorePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            if ("Invalid password provided".equals(e.getMessage())) {
                System.out.println("密码错误");
            }
            e.printStackTrace();
        }

        //获取到我的私钥
        BigInteger privateKeyInDec = credentials.getEcKeyPair().getPrivateKey();
        String privateKey = privateKeyInDec.toString(16);
        keyStore.setPrivateKey(privateKey);

        //获取到我的公钥
        BigInteger publicKeyInDec = credentials.getEcKeyPair().getPublicKey();
        String publicKey = publicKeyInDec.toString(16);
        keyStore.setPublicKey(publicKey);

        //获取到我的地址
        String address = credentials.getAddress();
        keyStore.setAddress(address);

        //打印
        System.out.println("address : " + address);
        System.out.println("publicKey ：" + publicKey);
        System.out.println("privateKey :" + privateKey);

        return keyStore;
    }

    /**
     * 将已经存在的私钥根据密码导如到本地存储中
     * @param privKey 私钥，这里为大数，不是字符串
     * @param password 为其设置的密码
     */
    private static void importPrivate(String privKey, String password) {
        //获取到本地存储密钥的路劲 主网为WalletUtils.getMainnetKeyDirectory()
        //这里是直接用配置类中的地址
        String directory = Config.filePath;
        BigInteger privateKey = new BigInteger(privKey,16);

        ECKeyPair ecKeyPair = ECKeyPair.create(privateKey);
        try {
            String fileName = WalletUtils.generateWalletFile(password,ecKeyPair,new File(directory),true);
            System.out.println("导入成功，文件名为 ：[" + fileName + "]" );
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//    public static void main(String[] args) {
//        Security.parseKeyFile("123","D:\\Ethereum\\testnet\\testData\\keystore\\UTC--2020-05-04T02-31-46.883620400Z--0d09412b42b39b1af641c6b39186065875b107fb.json");
//        /**
//         * address : 0x0d09412b42b39b1af641c6b39186065875b107fb
//         * publicKey ：1fb6132bd42fbd4ce72090cee380f541ea15169ad032a6bf633478346cbe036ad34a240c37798503868faad8863cddfa53486aa90d238013c8d0a87a0565d1fd
//         * privateKey :c92a7f6ece040104874589e032d40a878d5ba1e8ae89c302b6470c3873594edd
//         */
//        //Security.importPrivate(BigInteger.valueOf("c92a7f6ece040104874589e032d40a878d5ba1e8ae89c302b6470c3873594edd"),);
//        //String a = "c92a7f6ece040104874589e032d40a878d5ba1e8ae89c302b6470c3873594edd";
//        //Security.importPrivate(a,"123");
//    }
}
