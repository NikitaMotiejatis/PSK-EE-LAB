package lt.vu.components;

import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class RequestScopedCounter implements Serializable {

    @Getter
    private int count = 0;

    @Getter
    private String instanceId;

    @PostConstruct
    public void init() {
        this.instanceId = Integer.toHexString(System.identityHashCode(this));
        System.out.println("[RequestScoped] Created instance: " + instanceId);
    }

    public void increment() {
        count++;
        System.out.println("[RequestScoped] " + instanceId + " count = " + count);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[RequestScoped] Destroyed instance: " + instanceId);
    }
}
