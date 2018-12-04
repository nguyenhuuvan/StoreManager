package poly.project.storemanager.model;

public class BillDetail {
    private Integer billDetailID,quantity;
    private String billID,productID;

    public Integer getBillDetailID() {
        return billDetailID;
    }

    public void setBillDetailID(Integer billDetailID) {
        this.billDetailID = billDetailID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
