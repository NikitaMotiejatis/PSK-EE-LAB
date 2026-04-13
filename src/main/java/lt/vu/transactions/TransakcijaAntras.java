package lt.vu.transactions;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;

@RequestScoped
public class TransakcijaAntras {
    @Resource
    private TransactionSynchronizationRegistry tx;
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void doWork() {
        System.out.println("Antras txId: " + tx.getTransactionKey());
    }
}
