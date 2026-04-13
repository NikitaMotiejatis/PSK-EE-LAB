package lt.vu.persistence;

import lt.vu.entities.Player;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class PlayersDAO {

    @Inject
    private EntityManager em;

    public List<Player> loadAll() {
        return em.createNamedQuery("Player.findAll", Player.class).getResultList();
    }

    public void persist(Player player){
        this.em.persist(player);
    }

    public Player findOne(Integer id){
        return em.find(Player.class, id);
    }

    public Player update(Player player){
        return em.merge(player);
    }
}
