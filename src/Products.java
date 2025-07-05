import java.util.Date;
import java.util.HashMap;

public class Products {
    private static HashMap<String, Product> stock = new HashMap<>();
    public void addProduct(Product product) {
        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (stock.containsKey(product.getName())) {
            Product existingProduct = stock.get(product.getName());
            if (existingProduct.isExpirable()) {
                ExpirableProduct stockProduct = (ExpirableProduct) existingProduct;
                ExpirableProduct newProduct = (ExpirableProduct) product;
                HashMap<Date, Integer> stockExpireDates = stockProduct.getExpirationDates();
                if (stockExpireDates.containsKey(newProduct.getExpirationDate())) {
                    stockExpireDates.put(newProduct.getExpirationDate(), stockExpireDates.get(newProduct.getExpirationDate()) + product.getQuantity());
                } else {
                    stockExpireDates.put(newProduct.getExpirationDate(), product.getQuantity());
                }
                existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
            } else {
                existingProduct.setQuantity(existingProduct.getQuantity() + product.getQuantity());
            }
        } else {
            stock.put(product.getName(), product);
        }
    }
    public static HashMap<String, Product> getStock() {
        return stock;
    }
}