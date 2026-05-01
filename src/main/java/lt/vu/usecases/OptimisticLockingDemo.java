package lt.vu.usecases;

import lombok.Getter;
import lt.vu.entities.Player;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class OptimisticLockingDemo implements Serializable {

    @Inject
    private OptimisticLockingService service;

    @Getter
    private String log = "";

    public String run() {
        StringBuilder out = new StringBuilder();

        Integer playerId = service.findAnyPlayerId();
        if (playerId == null) {
            log = "No players in DB. Create one first.";
            return null;
        }
        out.append(String.format("Picked player id=%d%n", playerId));

        Player snapshot = service.loadDetachedSnapshot(playerId);
        out.append(String.format("[Step 1] User B loaded snapshot. name='%s', version=%d%n",
                snapshot.getName(), snapshot.getVersion()));

        Player afterA = service.otherUserCommits(playerId);
        out.append(String.format("[Step 2] User A committed an update. DB version is now %d%n",
                afterA.getVersion()));

        out.append(String.format("[Step 3] User B tries to merge stale snapshot (version=%d while DB=%d)%n",
                snapshot.getVersion(), afterA.getVersion()));
        try {
            service.attemptStaleUpdate(snapshot, " [B-stale]");
            out.append("Unexpected: no OptimisticLockException was thrown.\n");
        } catch (Exception e) {
            out.append(String.format("        Caught: %s%n", rootCause(e)));
            out.append("        -> JTA tx was marked for rollback; persistence context is invalid.\n");
            out.append("        -> Recovery requires a NEW transaction and a clean EM state.\n");
        }

        Player recovered = service.recover(playerId, " [B-recovered]");
        out.append(String.format("[Step 4] Recovery committed. Final: name='%s', version=%d%n",
                recovered.getName(), recovered.getVersion()));

        log = out.toString();
        return null;
    }

    private String rootCause(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null && c.getCause() != c) c = c.getCause();
        return c.getClass().getSimpleName() + ": " + c.getMessage();
    }
}
