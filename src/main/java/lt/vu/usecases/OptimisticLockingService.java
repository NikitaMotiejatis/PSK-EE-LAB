package lt.vu.usecases;

import lt.vu.entities.Player;
import lt.vu.persistence.PlayersDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

// Each step runs in its own JTA transaction (REQUIRES_NEW) so the demo can simulate two users.
@ApplicationScoped
public class OptimisticLockingService {

    @Inject
    private EntityManager em;

    @Inject
    private PlayersDAO playersDAO;

    public Integer findAnyPlayerId() {
        return playersDAO.loadAll().stream()
                .findFirst()
                .map(Player::getId)
                .orElse(null);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Player loadDetachedSnapshot(Integer playerId) {
        em.clear();
        Player p = em.find(Player.class, playerId);
        em.detach(p);
        return p;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Player otherUserCommits(Integer playerId) {
        em.clear();
        Player p = em.find(Player.class, playerId);
        p.setName("OL-A");
        em.flush();
        return p;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void attemptStaleUpdate(Player staleSnapshot) {
        em.clear();
        staleSnapshot.setName("OL-B-stale");
        em.merge(staleSnapshot);
        em.flush();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Player recover(Integer playerId) {
        em.clear();
        Player fresh = em.find(Player.class, playerId);
        fresh.setName("OL-B-recovered");
        em.flush();
        return fresh;
    }
}
