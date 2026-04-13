package lt.vu.usecases;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Player;
import lt.vu.entities.Tournament;
import lt.vu.persistence.PlayersDAO;
import lt.vu.persistence.TournamentsDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Model
public class Tournaments {

    @Inject
    private TournamentsDAO tournamentsDAO;

    @Inject
    private PlayersDAO playersDAO;

    @Getter @Setter
    private Tournament tournamentToCreate = new Tournament();

    @Getter
    private List<Tournament> allTournaments;

    @Getter
    private List<Player> allPlayers;

    @Getter @Setter
    private Integer selectedPlayerId;

    @PostConstruct
    public void init() {
        loadAllTournaments();
        this.allPlayers = playersDAO.loadAll();
    }

    @Transactional
    public String createTournament() {
        tournamentsDAO.persist(tournamentToCreate);
        return "tournaments?faces-redirect=true";
    }

    @Transactional
    public String addPlayerToTournament(Integer tournamentId) {
        Tournament tournament = tournamentsDAO.findOne(tournamentId);
        Player player = playersDAO.findOne(selectedPlayerId);
        tournament.getPlayers().add(player);
        tournamentsDAO.update(tournament);
        return "tournaments?faces-redirect=true";
    }

    private void loadAllTournaments() {
        this.allTournaments = tournamentsDAO.loadAll();
    }
}
