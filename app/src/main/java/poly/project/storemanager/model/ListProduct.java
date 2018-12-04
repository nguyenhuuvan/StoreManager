package poly.project.storemanager.model;

public class ListProduct {
    private final String id;
    private final String name;
    private final int quantity;
    private final byte[]imgProduct;

    public byte[] getImgProduct() {
        return imgProduct;
    }

    public ListProduct(String id, String name, int quantity, byte[] imgProduct) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.imgProduct = imgProduct;
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
