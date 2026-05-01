package lt.vu.extensibility;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import java.math.BigDecimal;

@Specializes
@ApplicationScoped
public class DiscountedPriceCalculator extends PriceCalculator {
    private static final BigDecimal DISCOUNT = new BigDecimal("0.90");

    @Override
    public BigDecimal finalPrice(BigDecimal listPrice) {
        return listPrice.multiply(DISCOUNT);
    }
}
