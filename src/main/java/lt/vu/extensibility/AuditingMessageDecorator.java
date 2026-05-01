package lt.vu.extensibility;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Decorator
public abstract class AuditingMessageDecorator implements MessageService {

    @Inject
    @Any
    @Delegate
    private MessageService delegate;

    @Override
    public String send(String to, String body) {
        String result = delegate.send(to, body);
        return "[AUDIT to=" + to + "] " + result;
    }
}
