import config.Config;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * 实现冷钱包的功能 离线创建钱包 签属交易
 */
public class ColdWallet {

    private Web3j web3j = Config.web3j;

    /**
     * 根据密码生成一个冷钱包  这个钱包生成后需要自己牢记密码和私钥 不会保存在任何地方
     * @param password 密码
     */
    public static void createColdWallet(String password) {
        String privateKey =null;
        String address = null;
        String publicKey = null;
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
            privateKey = privateKeyInDec.toString(16);
            WalletFile awallet = Wallet.createLight(password, ecKeyPair);
            address = awallet.getAddress();
            if (address.startsWith("ox")) {
                address = address.substring(2).toLowerCase();
            }else {
                address = address.toLowerCase();
            }
            address = "0x"+address;
            BigInteger publicKeyInDec = ecKeyPair.getPublicKey();
            publicKey = publicKeyInDec.toString(16);
            System.out.println("address : "+address);
            System.out.println("PrivKey : " + privateKey);
            System.out.println("PubKey : "+ publicKey);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("NoSuchAlgorithmException");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.out.println("NoSuchProviderException");
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            System.out.println("InvalidAlgorithmParameterException");
        } catch (CipherException e) {
            e.printStackTrace();
            System.out.println("CipherException");
        }

    }

    /**
     * 签属一个本地的交易 用于离线交易
     * @param nonce     最新的随机数
     * @param reciver   接收方地址
     * @param gasPrice  燃气价格
     * @param gasLimit  燃气限制
     * @param value     转账金额
     * @return      返回一笔交易的hash 只要发布这个hash就可以公布交易
     */
    public String singTransaction(String reciver,BigInteger nonce, BigInteger gasPrice,
                                  BigInteger gasLimit ,BigInteger value,String privateKey) {

        String hexValue = null;
        byte[] singedTransaction;
        //构造一笔离线交易
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce,gasPrice,gasLimit,reciver,value);
        //对私钥进行处理，并且成成一个对应的用于签名的证书
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);

        singedTransaction = TransactionEncoder.signMessage(rawTransaction,credentials);

        hexValue = Numeric.toHexString(singedTransaction);
        return  hexValue;
    }


    /**
     * 使用离线签名发送一笔交易
     * @param sender  发送方地址
     * @param reciver 接收方地址
     * @param value     金额
     * @param privateKey 密钥
     */
    public void sendRawTransaction(String sender, String reciver, String value, String privateKey) {

        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(sender, DefaultBlockParameterName.PENDING).send();

            if (ethGetTransactionCount == null){ return;}
            nonce = ethGetTransactionCount.getTransactionCount();

            //获取到转账的金额将eth 单位的转换为wei
            BigInteger amount = Convert.toWei(value,Convert.Unit.ETHER).toBigInteger();

            BigInteger gasPrice = Config.GAS_PRICE;
            BigInteger gasLimit = Config.GAS_LIMIT;

            String singData = singTransaction(reciver,nonce,gasPrice,gasLimit,amount,privateKey);

            if (singData!= null) {
                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(singData).send();
                String result= ethSendTransaction.getTransactionHash();
                System.out.println("发送交易成功！！！交易hash:[" + result +"]");
            }else {
                System.out.println("发送交易失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
