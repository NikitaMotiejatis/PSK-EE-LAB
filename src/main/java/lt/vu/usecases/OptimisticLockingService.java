package lt.vu.usecases;

import lt.vu.entities.Player;
import lt.vu.persistence.PlayersDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
        p.setName(p.getName() + " [other-user]");
        em.flush();
        return p;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void attemptStaleUpdate(Player staleSnapshot, String suffix) {
        em.clear();
        staleSnapshot.setName(staleSnapshot.getName() + suffix);
        em.merge(staleSnapshot);
        em.flush();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Player recover(Integer playerId, String suffix) {
        em.clear();
        Player fresh = em.find(Player.class, playerId);
        fresh.setName(fresh.getName() + suffix);
        em.flush();
        return fresh;
    }
}
