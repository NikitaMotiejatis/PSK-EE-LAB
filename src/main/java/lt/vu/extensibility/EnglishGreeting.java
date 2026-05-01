package lt.vu.extensibility;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnglishGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
