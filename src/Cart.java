import java.util.HashMap;

public class Cart {
    private HashMap<String, Integer> items;
    double subtotal;
    double shippingFees;
    Cart() {
        items = new HashMap<>();
        subtotal=0;
        shippingFees=0;
    }
    public void addProduct(String ProductName, int quantity) {
        HashMap<String, Product> stock = Products.getStock();
        if (!stock.containsKey(ProductName)) {
            throw new IllegalArgumentException("Product is not available in stock: " + ProductName);
        }
        Product stockProduct = stock.get(ProductName);
        if(quantity> stockProduct.getQuantity()) {
            throw new IllegalArgumentException("Requested quantity exceeds available quantity for product: " + ProductName + ". Available: " + stockProduct.getQuantity() + ", Requested: " + quantity);
        }
        if(quantity<= 0) {
            throw new IllegalArgumentException("Requested quantity must be positive: " + ProductName + ". Requested: " + quantity);
        }
        if (items.containsKey(ProductName)) {
            items.put(ProductName,items.get(ProductName)+ quantity);
            subtotal += stockProduct.getPrice() * quantity;
            if(stockProduct.isShippable()) {
                Shippable shippable = (Shippable) stockProduct;
                shippingFees += shippable.getWeight() * quantity;
            }
        } else {
            items.put(ProductName, quantity);
            subtotal += stockProduct.getPrice() *quantity;
        }
    }
    public HashMap<String, Integer> getItems() {
        return items;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public double getShippingFees() {
        return shippingFees;
    }
    public double getTotalPrice() {
        return subtotal + shippingFees;
    }
}
