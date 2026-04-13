package lt.vu.transactions;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;

@Named
@RequestScoped
public class TransakcijaPirmas {
    @Inject
    private TransakcijaAntras antras;

    @Resource
    private TransactionSynchronizationRegistry tx;

    @Transactional
    public void doWork() {
        System.out.println("Pirmas txId: " + tx.getTransactionKey());
        antras.doWork();
    }
}
