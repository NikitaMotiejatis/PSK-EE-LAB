package lt.vu.persistence;

import lt.vu.entities.Tournament;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class TournamentsDAO {

    @Inject
    private EntityManager em;

    public List<Tournament> loadAll() {
        return em.createNamedQuery("Tournament.findAll", Tournament.class).getResultList();
    }

    public void persist(Tournament tournament) {
        this.em.persist(tournament);
    }

    public Tournament findOne(Integer id) {
        return em.find(Tournament.class, id);
    }

    public Tournament update(Tournament tournament) {
        return em.merge(tournament);
    }
}
