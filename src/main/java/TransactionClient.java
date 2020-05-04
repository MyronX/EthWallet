import config.Config;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 热钱包 在线交易
 */
public class TransactionClient {


    private Web3j web3j = Config.web3j;


    /**
     * 构建一笔交易
     * @param sender 发送方地址
     * @param nonce     最新的随机数
     * @param reciver   接收方地址
     * @param gasPrice  燃气价格
     * @param gasLimit  燃气限制
     * @param value     转账金额
     * @return      返回一笔交易
     */
    private Transaction makeTransaction(String sender,BigInteger nonce, String reciver, BigInteger gasPrice,
                                        BigInteger gasLimit ,BigInteger value) {
        return Transaction.createEtherTransaction(sender,nonce,gasPrice,gasLimit,reciver,value);
    }


    /**
     * 发送一笔交易
     * @param sender 发送方地址
     * @param reciver   接收方地址
     * @param value     转账金额
     * @param password  账户密码
     */
    private void sendTransaction(String sender,String reciver, String value,String password) {

        //获取到转账的金额将eth 单位的转换为wei
        BigInteger amount = Convert.toWei(value,Convert.Unit.ETHER).toBigInteger();
        String txHash = null;
        EthSendTransaction ethSendTransaction = null;
        //解锁账户
        boolean isUnlock = AccountManager.unlockAccount(sender,password);
        //解锁完毕 发送交易
        if (isUnlock) {
            BigInteger nonce = Info.getNonce(sender);
            BigInteger gasPrice = Config.GAS_PRICE;
            BigInteger gasLimit = Config.GAS_LIMIT;
            Transaction transaction = makeTransaction(sender,nonce,reciver,gasPrice,gasLimit,amount);

            //发送交易
            try {
                ethSendTransaction = web3j.ethSendTransaction(transaction).send();
            } catch (IOException e) {
                System.out.println("发送失败 ：" + e);
                e.printStackTrace();
            }
        }
        //获取到交易的hash值
        if (ethSendTransaction!= null) {
            txHash= ethSendTransaction.getTransactionHash();
            System.out.println("交易发送给成功，TxHash ：["+ txHash + "]");
        }
        //再一次锁定账户
        AccountManager.lockAccount(sender);

    }

}
