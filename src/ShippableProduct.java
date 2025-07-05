public class ShippableProduct extends Product implements Shippable {
    private double weight;

    public ShippableProduct() {
        super("Default Product", 0.0, 0);
        this.weight = 0.0;
    }

    public ShippableProduct(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
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

}