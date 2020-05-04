import com.sun.media.sound.AbstractMidiDeviceProvider;
import config.Config;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.geth.Geth;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * 管理账户相关
 */
public class AccountManager {

    private static Admin admin = Config.admin;

    private static Geth geth = Config.geth;

    /**
     * 存放生成的账户对应的keystore路径
     */
    private String keystorePath = "../keystore/";


    /**
     * 获取本地节点的所有账户
     */
    public void getAccountsList(){
        try {
            PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
            List<String> addressList = personalListAccounts.getAccountIds();
            System.out.println("获取到的账户总数 ：" + addressList.size());

            for (int i = 1; i <= addressList.size() ; i++) {
                System.out.println("第"+i+"个账户的地址为：[" + addressList.get(i-1) + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解锁账户 发送交易之前需要进行的操作
     * @param address 解锁的地址
     * @param password 对应账户的地址
     */
    public static boolean unlockAccount(String address, String password) {
        //账户的解锁时间，即在该时间内发起交易无需再次解锁 单位为秒
        BigInteger unlockTime = BigInteger.valueOf(300L);
        boolean isUnlock = false;
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password, unlockTime).send();
            isUnlock = personalUnlockAccount.accountUnlocked();
            System.out.println("address unlock ；"+isUnlock);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isUnlock;
    }


    /**
     * 堆某个账户地址进行锁定
     * @param address 需要上锁的地址
     */
    public static boolean lockAccount(String address) {

        boolean isLock = false;
        try {
            BooleanResponse response =   geth.personalLockAccount(address).send();
            //判断是否上锁
            isLock = response.success();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isLock;
    }

    /**
     * 根据密码创建一个账户 同时会显示对应的信息
     * 这个采用的是和以太坊一致的生成方法，用户只能获取到地址
     * @param password 输入客户的密码
     */
    public void creatNewAccount(String password) {
        try {
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
            String address = newAccountIdentifier.getAccountId();
            System.out.println("请牢记您的密码！！！");
            System.out.println("创建账户成功，地址为: ["+ address +"]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //冷钱包的实现部分在下面

}
