import config.Config;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 查看当前连接的链的信息
 */
public class Info {

    private static Web3j web3j = Config.web3j;

    /**
     * 获取链上的完整信息
     */
    public void getEthInfo() {

        try {
            //客户端版本
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();

            //区块数量
            EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
            BigInteger blockNumber = ethBlockNumber.getBlockNumber();

            //挖矿奖励账户
            EthCoinbase ethCoinbase = web3j.ethCoinbase().send();
            String coinbaseAddress = ethCoinbase.getAddress();

            //是否在同步区块
            EthSyncing ethSyncing = web3j.ethSyncing().send();
            boolean isSyncing = ethSyncing.isSyncing();

            //是否在挖矿
            EthMining ethMining = web3j.ethMining().send();
            boolean isMining = ethMining.isMining();

            //当前gas price
            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();

            //挖矿速度
            EthHashrate ethHashrate = web3j.ethHashrate().send();
            BigInteger hashRate = ethHashrate.getHashrate();

            //协议版本
            EthProtocolVersion ethProtocolVersion = web3j.ethProtocolVersion().send();
            String protocolVersion = ethProtocolVersion.getProtocolVersion();

            //连接的节点数
            NetPeerCount netPeerCount = web3j.netPeerCount().send();
            BigInteger peerCount = netPeerCount.getQuantity();

            System.out.println("全网状态 ：");
            System.out.println("客户端版本 ：" + clientVersion + "\n" +
                    "区块数量 ： " + blockNumber + "\n" +
                    "币基账户 ： " + coinbaseAddress + "\n" +
                    "同步状态 ： " + isSyncing + "\n" +
                    "挖矿状态 ： " + isMining + "\n" +
                    "gas 价格 ： " + gasPrice + "\n" +
                    "算    力 ： " + hashRate + "\n" +
                    "协议版本 ： " + protocolVersion + "\n" +
                    "连接节点数 ： " + peerCount + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据传入的地址进行余额的查找
     * @param address 要查询的地址
     */
    public void getBalance(String address){
        BigInteger balance = null;

        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            balance = ethGetBalance.getBalance();
            System.out.println("地址：[" + address +"]对应的余额为 ：" + balance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据一个交易的hash 查询完整的交易
     * @param hash 交易hash
     */
    public void getTxByHash(String hash) {
        try {
            EthTransaction ethTransaction = web3j.ethGetTransactionByHash(hash).send();
            System.out.println(ethTransaction.getTransaction());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据区块hash 查询一个区块的信息
     * @param hash 区块的hash
     */
    public void getBlockByHash(String hash) {
        try {
            EthBlock ethBlock = web3j.ethGetBlockByHash(hash,true).send();
            System.out.println(ethBlock);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据高度查询一个区块
     * @param heigh 区块所在的高度
     */
    public void getBlockByHeigh(Integer heigh) {
        DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(heigh);
        try {
            EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter,true).send();
            System.out.println(ethBlock);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过地址获取到对应的nonce值
     * @param address 账户地址
     * @return 当前最新的nonce值
     */
    public static BigInteger getNonce(String address) {
        BigInteger nonce = BigInteger.ZERO;

        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            nonce = ethGetTransactionCount.getTransactionCount();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nonce;
    }
}
