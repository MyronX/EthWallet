import config.Config;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.geth.Geth;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

/**
 * 程序的主启动类
 * @author MyronX
 */
public class main {

    public static void main(String[] args) {


        //初始化三个管理对象
        Web3j web3j = Web3j.build(new HttpService(Config.RPC_URL));
        Admin admin = Admin.build(new HttpService(Config.RPC_URL));
        Geth geth = Geth.build(new HttpService(Config.RPC_URL));

    }
}
