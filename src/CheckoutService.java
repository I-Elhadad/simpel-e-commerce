import java.util.*;

public class CheckoutService {
    void deleteExpiredProducts() {
        for (String productName : Products.getStock().keySet()) {
            Product stockProduct = Products.getStock().get(productName);
            if (stockProduct.isExpirable()) {
                ExpirableProduct expirableProduct = (ExpirableProduct) stockProduct;
                List<Date> expiredDates = new ArrayList<>();
                for (Date expirationDate : expirableProduct.getExpirationDates().keySet()) {
                    if (expirableProduct.isExpired(expirationDate)) {
                        stockProduct.setQuantity(stockProduct.getQuantity() - expirableProduct.getExpirationDates().get(expirationDate));
                        expiredDates.add(expirationDate);
                    }
                }
                for (Date expiredDate : expiredDates) {
                    expirableProduct.getExpirationDates().remove(expiredDate);
                }
            }
        }
    }

    public void Checkout(Customer customer) {
        deleteExpiredProducts();
        if (customer.getCart().getItems().isEmpty()) {
            System.out.println("Your cart is empty. Please add products to your cart before checking out.");
            return;
        }
        double totalAmount = customer.getCart().getTotalPrice();
        if (totalAmount > customer.getBalance()) {
            System.out.println("Total amount: " + totalAmount);
            System.out.println("Your current balance: " + customer.getBalance());
            System.out.println("Insufficient balance. Please add funds to your account.");
            return;
        }
        for (String productName : customer.getCart().getItems().keySet()) {
            Product stockProduct = Products.getStock().get(productName);
                int cartQuantity = customer.getCart().getItems().get(productName);
                if (cartQuantity > stockProduct.getQuantity()) {
                    throw new IllegalArgumentException("Quantity of " + productName + " in cart (" + cartQuantity + ") exceeds available stock (" + stockProduct.getQuantity() + ").");

            }
        }
        double subtotal = customer.getCart().getSubtotal();
        double shipping = customer.getCart().getShippingFees();

        HashMap<String, Integer> purchasedItems = new HashMap<>(customer.getCart().getItems());

        System.out.println("** Shipment notice **");
        double totalWeight = 0.0;
        for (String productName : purchasedItems.keySet()) {
            Product stockProduct = Products.getStock().get(productName);
            int qty = purchasedItems.get(productName);
            if (stockProduct.isShippable()) {
                Shippable shipProd = (Shippable) stockProduct;
                double weight = shipProd.getWeight();
                System.out.println(qty + "x " + productName + " " + (weight * 1000) + "g");
                totalWeight += weight * qty;
            }
        }
        System.out.printf("Total package weight %.1fkg\n", totalWeight);

        // Checkout receipt
        System.out.println("** Checkout receipt **");
        for (String productName : purchasedItems.keySet()) {
            int qty = purchasedItems.get(productName);
            Product stockProduct = Products.getStock().get(productName);
            int price = (int) stockProduct.getPrice();
            System.out.println(qty + "x " + productName + " " + (price * qty));
        }
        System.out.println("----------------------");
        System.out.println("Subtotal " + (int) subtotal);
        System.out.println("Shipping " + (int) shipping);
        System.out.println("Amount " + (int) totalAmount);
        for (String productName : purchasedItems.keySet()) {
            int cartQuantity = purchasedItems.get(productName);
            Product stockProduct = Products.getStock().get(productName);
            if (stockProduct.isExpirable()) {
                ExpirableProduct expProduct = (ExpirableProduct) stockProduct;
                List<Date> dates = new ArrayList<>(expProduct.getExpirationDates().keySet());
                dates.sort(Date::compareTo);
                for (Date date : dates) {
                    int available = expProduct.getExpirationDates().get(date);
                    if (cartQuantity <= 0) break;
                    if (available <= cartQuantity) {
                        cartQuantity -= available;
                        expProduct.setQuantity(expProduct.getQuantity() - available);
                        expProduct.getExpirationDates().remove(date);
                    } else {
                        expProduct.getExpirationDates().put(date, available - cartQuantity);
                        expProduct.setQuantity(expProduct.getQuantity() - cartQuantity);
                        cartQuantity = 0;
                    }
                }
            } else {
                stockProduct.setQuantity(stockProduct.getQuantity() - cartQuantity);
            }
        }
        customer.getCart().getItems().clear();
        customer.setBalance(customer.getBalance() - totalAmount);
    }
}
