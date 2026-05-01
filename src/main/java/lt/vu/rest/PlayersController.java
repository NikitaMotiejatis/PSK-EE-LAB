package lt.vu.rest;

import lombok.*;
import lt.vu.entities.Team;
import lt.vu.persistence.TeamsDAO;
import lt.vu.rest.contracts.PlayerDto;
import lt.vu.entities.Player;
import lt.vu.persistence.PlayersDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@ApplicationScoped
@Path("/players")
public class PlayersController {

    @Inject
    @Setter @Getter
    private PlayersDAO playersDAO;

    @Inject
    @Setter @Getter
    private TeamsDAO teamsDAO;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Integer id) {
        Player player = playersDAO.findOne(id);
        if (player == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PlayerDto playerDto = new PlayerDto();
        playerDto.setName(player.getName());
        playerDto.setJerseyNumber(player.getJerseyNumber());
        playerDto.setTeamName(player.getTeam().getName());
        playerDto.setTeamId(player.getTeam().getId());

        return Response.ok(playerDto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(PlayerDto playerData) {
        if (playerData.getTeamId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("teamId is required").build();
        }
        Team team = teamsDAO.findOne(playerData.getTeamId());
        if (team == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("team not found").build();
        }

        Player player = new Player();
        player.setName(playerData.getName());
        player.setJerseyNumber(playerData.getJerseyNumber());
        player.setTeam(team);
        playersDAO.persist(player);

        URI location = UriBuilder.fromResource(PlayersController.class)
                .path("/{id}")
                .build(player.getId());
        return Response.created(location).entity(playerData).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(
            @PathParam("id") final Integer playerId,
            PlayerDto playerData) {
        try {
            Player existingPlayer = playersDAO.findOne(playerId);
            if (existingPlayer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            existingPlayer.setName(playerData.getName());
            existingPlayer.setJerseyNumber(playerData.getJerseyNumber());
            playersDAO.update(existingPlayer);
            return Response.ok().build();
        } catch (OptimisticLockException ole) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
