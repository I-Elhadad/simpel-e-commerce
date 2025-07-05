
import java.util.Date;
public class Main {
    public static void main(String[] args) {
        testInsufficientBalance();
        testLimitedStock();
        testExpire1();
        testExpire2();
        invalidInputTest();
        testProductEqualityInCart();
        scenarioExpirableShippableProduct();
        RegularProduct();
        mixExpirableProductAndShippableProduct();
    }

    public static void clearStock() {
        Products.getStock().clear();
    }

    public static void testInsufficientBalance() {
        try {
            clearStock();
            Products products = new Products();
            products.addProduct(new ExpirableProduct("Milk", 3.0, 10, new Date(System.currentTimeMillis() + 5 * 86400000)));
            products.addProduct(new ShippableProduct("Laptop", 1000.0, 2, 2.5));
            System.out.println("=== Scenario 1 ===");
            Customer customer1 = new Customer("Bob", 200.0);
            customer1.getCart().addProduct("Milk", 2);
            customer1.getCart().addProduct("Laptop", 1);
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.Checkout(customer1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void testLimitedStock() {
        try {
            clearStock();
            Products products = new Products();
            products.addProduct(new ExpirableProduct("Bread", 1.5, 2, new Date(System.currentTimeMillis() + 2 * 86400000)));
            System.out.println("=== Scenario 2 ===");
            Customer customer2 = new Customer("Eve", 50.0);
            customer2.getCart().addProduct("Bread", 3); // Only 2 in stock, should throw
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void testExpire1() {
        try {
            clearStock();
            Products products = new Products();
            products.addProduct(new ExpirableProduct("Yogurt", 2.0, 5, new Date(System.currentTimeMillis() + 3 * 86400000)));
            System.out.println("=== Scenario 3 ===");
            Customer customer3 = new Customer("Sam", 20.0);
            customer3.getCart().addProduct("Yogurt", 1);
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.Checkout(customer3);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void testExpire2() {
        // expired Yogurt no valid Yogurt
        try {
            clearStock();
            Products products = new Products();
            products.addProduct(new ExpirableProduct("Yogurt", 2.0, 5, new Date(System.currentTimeMillis() - 3 * 86400000)));
            System.out.println("=== Scenario 4 ===");
            Customer customer3 = new Customer("Sam", 20.0);
            customer3.getCart().addProduct("Yogurt", 1);
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.Checkout(customer3);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void invalidInputTest() {
        try {
            clearStock();
            Products products = new Products();
            products.addProduct(new Product("InvalidProduct", 10.0, 1));
            System.out.println("=== Scenario 5 ===");
            Customer customer7 = new Customer("InvalidUser", 0.0);
            customer7.getCart().addProduct("InvalidProduct", -1); // Invalid quantity
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.Checkout(customer7);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void testProductEqualityInCart() {
        try {
            clearStock();
            Products products = new Products();
            products.addProduct(new Product("Pen", 1.0, 10));
            System.out.println("=== Scenario 6 ===");
            Customer customer = new Customer("TestUser", 100.0);
            customer.getCart().addProduct("Pen", 2);
            customer.getCart().addProduct("Pen", 3); // Should update quantity
            System.out.println("Cart size (should be 1): " + customer.getCart().getItems().size());
            System.out.println("Total quantity of Pen: " + customer.getCart().getItems().get("Pen"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void RegularProduct() {
        try {
            clearStock();
            Products products = new Products();
            products.addProduct(new Product("Pen", 1.0, 10));
            products.addProduct(new Product("Book", 8.0, 5));
            System.out.println("=== Scenario 7 ===");
            Customer customer = new Customer("Alice", 100.0);
            customer.getCart().addProduct("Pen", 3);
            customer.getCart().addProduct("Book", 2);
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.Checkout(customer);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void mixExpirableProductAndShippableProduct() {
        try {
            clearStock();
            Products products = new Products();
            products.addProduct(new ExpirableProduct("Cheese", 4.0, 5, new Date(System.currentTimeMillis() + 7 * 86400000)));
            products.addProduct(new ShippableProduct("Notebook", 5.0, 10, 0.3));
            System.out.println("=== Scenario 8 ===");
            Customer customer = new Customer("Ben", 200.0);
            customer.getCart().addProduct("Cheese", 2);
            customer.getCart().addProduct("Notebook", 4);
            System.out.println("Subtotal: " + customer.getCart().getSubtotal());
            System.out.println("Shipping fees: " + customer.getCart().getShippingFees());
            System.out.println("Total: " + customer.getCart().getTotalPrice());
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.Checkout(customer);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void scenarioExpirableShippableProduct() {
        try {
            clearStock();
            Products products = new Products();
            Date futureDate = new Date(System.currentTimeMillis() + 5 * 86400000);
            products.addProduct(new ExpirableShippableProduct("Vaccine", 100.0, 3, futureDate, 0.05));
            System.out.println("=== Scenario 9 ===");
            Customer customer = new Customer("Dr. Smith", 500.0);
            customer.getCart().addProduct("Vaccine", 2);
            System.out.println("Subtotal: " + customer.getCart().getSubtotal());
            System.out.println("Shipping fees: " + customer.getCart().getShippingFees());
            System.out.println("Total: " + customer.getCart().getTotalPrice());
            CheckoutService checkoutService = new CheckoutService();
            checkoutService.Checkout(customer);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}