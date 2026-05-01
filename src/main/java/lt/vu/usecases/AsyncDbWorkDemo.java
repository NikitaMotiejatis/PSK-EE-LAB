package lt.vu.usecases;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Named
@SessionScoped
public class AsyncDbWorkDemo implements Serializable {

    @Inject
    private AsyncDbWorker worker;

    private CompletableFuture<String> task;

    public String start() {
        task = CompletableFuture.supplyAsync(worker::doLongDbWork);
        return null;
    }

    public boolean isRunning() {
        return task != null && !task.isDone();
    }

    public String getStatus() throws ExecutionException, InterruptedException {
        if (task == null) {
            return "Not started";
        }
        if (!task.isDone()) {
            return "Running on a worker thread...";
        }
        return task.get();
    }
}
