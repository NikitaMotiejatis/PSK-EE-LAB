package lt.vu.components;

import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class SessionScopedCounter implements Serializable {

    @Getter
    private int count = 0;

    @Getter
    private String instanceId;

    @PostConstruct
    public void init() {
        this.instanceId = Integer.toHexString(System.identityHashCode(this));
        System.out.println("[SessionScoped] Created instance: " + instanceId);
    }

    public void increment() {
        count++;
        System.out.println("[SessionScoped] " + instanceId + " count = " + count);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[SessionScoped] Destroyed instance: " + instanceId);
    }
}
