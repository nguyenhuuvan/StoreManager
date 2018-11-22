package poly.vannhph06247.storemanager.model;

public class ListProduct {
    private final String id;
    private final String name;
    private final int quantity;

    public ListProduct(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

}
