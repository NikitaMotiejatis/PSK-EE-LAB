package lt.vu.usecases;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.SynchronizationType;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

@ApplicationScoped
public class AsyncDbWorker {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Resource
    private UserTransaction utx;

    public String doLongDbWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
        try {
            utx.begin();
            em.joinTransaction();
            Long count = em.createQuery("select count(p) from Player p", Long.class).getSingleResult();
            utx.commit();
            return "Players in DB: " + count + " (counted on thread " + Thread.currentThread().getName() + ")";
        } catch (Exception e) {
            try {
                if (utx.getStatus() != Status.STATUS_NO_TRANSACTION
                        && utx.getStatus() != Status.STATUS_COMMITTED) {
                    utx.rollback();
                }
            } catch (Exception ignored) {
            }
            return "Failed: " + e.getClass().getSimpleName() + ": " + e.getMessage();
        } finally {
            em.close();
        }
    }
}
