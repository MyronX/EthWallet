import config.Config;
import org.reactivestreams.Subscription;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;


/**
 *监听区块上的信息
 */
public class Filter {
    private static Web3j web3j = Config.web3j;

    /**
     * 监听新区快
     */
    private static void newBlockFilter() {
        Subscription subscription = (Subscription) web3j.
                blockFlowable(false).
                subscribe(block -> {
                    System.out.println("receive a new Blcok");
                    System.out.println("block number" + block.getBlock().getNumber());

        });
    }

    /**
     * 监听新交易
     */
    private static void newTransactionFilter() {
        Subscription subscription = (Subscription) web3j.
                transactionFlowable().
                subscribe(tx -> {
                    System.out.println("receive a new Transaction");
                    System.out.println("Transaction hash :[" + tx.getBlockHash() + "]" );
                });
    }

    /**
     * 监听pengding交易
     */
    private static void newPendingTx() {
        Subscription subscription = (Subscription)web3j.
                pendingTransactionFlowable().
                subscribe(tx -> {
                    System.out.println("receive a new PendingTransaction");
                    System.out.println("PendingTransaction hash :[" + tx.getBlockHash() + "]" );
                });
    }


}
