package poly.project.storemanager.model;

public class ListProductOfCustomer {
    private final String productId;
    private String productName;
    private int quantity;
    private final long dateBill;
    private final int price;
    private byte[]imgProduct;

    public int getPrice() {
        return price;
    }

    public ListProductOfCustomer(String productId, String productName, int quantity, long dateBill, int price, byte[] imgProduct) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.dateBill = dateBill;
        this.price = price;
        this.imgProduct = imgProduct;
    }
    public byte[] getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(byte[] imgProduct) {
        this.imgProduct = imgProduct;
    }

    public String getProductId() {
        return productId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getDateBill() {
        return dateBill;
    }
}
