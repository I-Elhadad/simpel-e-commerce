import java.util.Date;
import java.util.HashMap;

public class ExpirableProduct extends Product
{
    private Date expirationDate;
   private HashMap<Date,Integer>expirationDates;
    public ExpirableProduct(String name, double price, int quantity, Date expirationDate) {
        super(name, price, quantity);
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date cannot be null");
        }
        this.expirationDate = expirationDate;
        this.expirationDates = new HashMap<>();
        this.expirationDates.put(expirationDate, quantity);
    }
    public Date getExpirationDate() {
        return expirationDate;
    }
    public HashMap<Date, Integer> getExpirationDates() {
        return expirationDates;
    }

    public boolean isExpired(Date expiredate)
    {
        Date currentDate = new Date();
        return !expiredate.after(currentDate);
    };
    @Override
    public boolean isExpirable()
    {
        return true;
    }

}
