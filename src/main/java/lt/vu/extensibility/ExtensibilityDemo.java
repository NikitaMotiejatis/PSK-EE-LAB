package lt.vu.extensibility;

import lombok.Getter;
import lt.vu.interceptors.LoggedInvocation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

@Named
@RequestScoped
@Getter
public class ExtensibilityDemo implements Serializable {

    @Inject
    private Greeting greeting;

    @Inject
    private PriceCalculator priceCalculator;

    @Inject
    private MessageService messageService;

    private String interceptorMessage = "(not yet run)";

    public String getGreetingResult() {
        return greeting.greet("studentas") + "  (impl: " + greeting.getClass().getSimpleName() + ")";
    }

    public String getPriceResult() {
        BigDecimal raw = new BigDecimal("100.00");
        return raw + " -> " + priceCalculator.finalPrice(raw)
                + "  (impl: " + priceCalculator.getClass().getSimpleName() + ")";
    }

    public String getMessageResult() {
        return messageService.send("alice", "hi") + "  (impl: " + messageService.getClass().getSimpleName() + ")";
    }

    @LoggedInvocation
    public String runInterceptedAction() {
        interceptorMessage = "intercepted action ran at " + LocalTime.now()
                + " (check the WildFly console for 'Called method: runInterceptedAction')";
        return null;
    }
}
