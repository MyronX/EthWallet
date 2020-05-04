package config;

import lombok.Data;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.geth.Geth;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

/**
 * 运行配置
 */
@Data
public class Config {

    //本地的节点环境，可以替换成远程服务节点地址
    public static String RPC_URL = "http://127.0.0.1:8545";

    //存放生成的keystore文件路径 这里应该是节点保存keystore 的地址
    public static String filePath = "D:/Ethereum/testnet/testData/keystore/";

    //设定燃气值
    public static final BigInteger GAS_PRICE = new BigInteger("22000000");
    public static final BigInteger GAS_LIMIT = new BigInteger("21000");

    public static Web3j web3j = Web3j.build(new HttpService(Config.RPC_URL));
    public static Admin admin = Admin.build(new HttpService(Config.RPC_URL));
    public static Geth geth = Geth.build(new HttpService(Config.RPC_URL));
}
