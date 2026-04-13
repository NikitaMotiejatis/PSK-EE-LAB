package lt.vu.usecases;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.Player;
import lt.vu.entities.Tournament;
import lt.vu.persistence.PlayersDAO;
import lt.vu.persistence.TournamentsDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
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
    public String addPlayerToTournament() {
        String tournamentIdParam = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("tournamentId");
        String playerIdParam = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("playerId");
        Integer tournamentId = Integer.valueOf(tournamentIdParam);
        Integer playerId = Integer.valueOf(playerIdParam);
        Tournament tournament = tournamentsDAO.findOne(tournamentId);
        Player player = playersDAO.findOne(playerId);
        if (!tournament.getPlayers().contains(player)) {
            tournament.getPlayers().add(player);
            tournamentsDAO.update(tournament);
        }
        return "tournaments?faces-redirect=true";
    }

    private void loadAllTournaments() {
        this.allTournaments = tournamentsDAO.loadAll();
    }
}
