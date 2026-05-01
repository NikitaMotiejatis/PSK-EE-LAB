package lt.vu.extensibility;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;

@ApplicationScoped
public class PriceCalculator {
    public BigDecimal finalPrice(BigDecimal listPrice) {
        return listPrice;
    }
}
