package lt.vu.components;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AntrasKomponentas {

    public String sakykLabas() {
        return "Labas is Antras " + toString();
    }
}

