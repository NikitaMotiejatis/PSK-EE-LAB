package lt.vu.components;

import org.w3c.dom.ls.LSOutput;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Named
@SessionScoped //@RequestScoped
public class PirmasKomponentas implements java.io.Serializable {

    public String sakykLabas() {
        return "Labas " + new Date() + " " + toString();
    }

    // Field injection
    @Inject
    private AntrasKomponentas antras;

    // Constructor injection
    @Inject
    public PirmasKomponentas(AntrasKomponentas antras) {
        this.antras = antras;
    }

    @PostConstruct
    public void init() {
        System.out.println(antras.getClass().getName());
    }

    @PreDestroy
    public void aboutToDie() {
        System.out.println(toString() + " ready to die.");
    }
}