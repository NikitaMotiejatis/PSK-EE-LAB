package lt.vu.usecases;

import lombok.Getter;
import lombok.Setter;
import lt.vu.mybatis.dao.PlayerMapper;
import lt.vu.mybatis.dao.TournamentMapper;
import lt.vu.mybatis.model.Player;
import lt.vu.mybatis.model.Tournament;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Model
public class TournamentsMyBatis {

    @Inject
    private TournamentMapper tournamentMapper;

    @Inject
    private PlayerMapper playerMapper;

    @Getter
    private List<Tournament> allTournaments;

    @Getter
    private List<Player> allPlayers;

    @Getter @Setter
    private Tournament tournamentToCreate = new Tournament();

    @Getter @Setter
    private Integer selectedPlayerId;

    @PostConstruct
    public void init() {
        loadAllTournaments();
        this.allPlayers = playerMapper.selectAll();
    }

    private void loadAllTournaments() {
        this.allTournaments = tournamentMapper.selectAll();
    }

    @Transactional
    public String createTournament() {
        tournamentMapper.insert(tournamentToCreate);
        return "/myBatis/tournaments?faces-redirect=true";
    }

    @Transactional
    public String addPlayerToTournament(Integer tournamentId) {
        tournamentMapper.insertPlayerTournament(tournamentId, selectedPlayerId);
        return "/myBatis/tournaments?faces-redirect=true";
    }
}
