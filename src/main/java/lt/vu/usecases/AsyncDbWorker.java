package lt.vu.usecases;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.SynchronizationType;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * Demonstrates async-friendly DB access.
 *
 * Why @Resource UserTransaction would NOT work here:
 *   This bean is lazily created the first time it's touched, and that
 *   first touch happens on a CompletableFuture worker thread (ForkJoinPool).
 *   Worker threads have no java:comp/ JNDI namespace, so injection of
 *   @Resource UserTransaction fails with NameNotFoundException.
 *
 * Fix: look up the global java:jboss/UserTransaction inside the method.
 * Global JNDI names work from any thread.
 *
 * Same reason we use @PersistenceUnit (EntityManagerFactory) and create
 * our own EM here, instead of injecting the @RequestScoped EM produced
 * by lt.vu.persistence.Resources — the request scope is not active on
 * the worker thread.
 */
@ApplicationScoped
public class AsyncDbWorker {

    @PersistenceUnit
    private EntityManagerFactory emf;

    public String doLongDbWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        UserTransaction utx;
        try {
            utx = (UserTransaction) new InitialContext().lookup("java:jboss/UserTransaction");
        } catch (NamingException e) {
            return "Failed JNDI lookup of UserTransaction: " + e.getMessage();
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
