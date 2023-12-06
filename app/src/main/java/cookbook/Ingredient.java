package cookbook;

public class Ingredient {
    private String name;
    private double amount;
    private String unit;
    private int id;
    private int shopId;


    public Ingredient(String name, double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public Ingredient(String name, double amount, String unit, int id) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.id = id;
    }

    public Ingredient(String name, double amount) {
        this.name = name;
        this.amount = amount;
       
    }
    
    public Ingredient(String name, double amount, int id, int shopId) {
        this.name = name;
        this.amount = amount;
        this.id = id;
       this.shopId = shopId;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopID(){
        return shopId;
    }
    
}
