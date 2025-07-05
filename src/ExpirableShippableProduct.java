import java.util.Date;

public class ExpirableShippableProduct extends ExpirableProduct implements Shippable {
    private double weight;

    public ExpirableShippableProduct(String name, double price, int quantity, Date expirationDate, double weight) {
        super(name, price, quantity, expirationDate);
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        this.weight = weight;
    }

    @Override
    public boolean isShippable() {
        return true;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    public double getShippingFees() {
        return weight;
    }
}