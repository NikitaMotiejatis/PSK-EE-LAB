package lt.vu.extensibility;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@Alternative
@ApplicationScoped
public class LithuanianGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Labas, " + name + "!";
    }
}
