package lt.vu.extensibility;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BasicMessageService implements MessageService {
    @Override
    public String send(String to, String body) {
        return "Sent to=" + to + " body='" + body + "'";
    }
}
