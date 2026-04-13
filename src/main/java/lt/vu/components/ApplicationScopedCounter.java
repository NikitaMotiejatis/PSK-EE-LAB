package lt.vu.components;

import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ApplicationScoped
public class ApplicationScopedCounter implements Serializable {

    @Getter
    private int count = 0;

    @Getter
    private String instanceId;

    @PostConstruct
    public void init() {
        this.instanceId = Integer.toHexString(System.identityHashCode(this));
        System.out.println("[ApplicationScoped] Created instance: " + instanceId);
    }

    public void increment() {
        count++;
        System.out.println("[ApplicationScoped] " + instanceId + " count = " + count);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[ApplicationScoped] Destroyed instance: " + instanceId);
    }
}
