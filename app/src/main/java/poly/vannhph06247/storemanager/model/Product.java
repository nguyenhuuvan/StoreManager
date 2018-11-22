package poly.vannhph06247.storemanager.model;

public class Product {
    private String productID, productName, productTypeID, description;
    private int inputPrice, outputPrice;
    private int quantity;
    private String oldProductName, oldProductTypeID, oldDescription;
    private int oldInputPrice, oldOutputPrice;
    private int oldQuantity;
    private long AddedDay;
    private long EditedDay;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTypeID() {
        return productTypeID;
    }

    public void setProductTypeID(String productTypeID) {
        this.productTypeID = productTypeID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInputPrice() {
        return inputPrice;
    }

    public void setInputPrice(int inputPrice) {
        this.inputPrice = inputPrice;
    }

    public int getOutputPrice() {
        return outputPrice;
    }

    public void setOutputPrice(int outputPrice) {
        this.outputPrice = outputPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOldProductName() {
        return oldProductName;
    }

    public void setOldProductName(String oldProductName) {
        this.oldProductName = oldProductName;
    }

    public String getOldProductTypeID() {
        return oldProductTypeID;
    }

    public void setOldProductTypeID(String oldProductTypeID) {
        this.oldProductTypeID = oldProductTypeID;
    }

    public String getOldDescription() {
        return oldDescription;
    }

    public void setOldDescription(String oldDescription) {
        this.oldDescription = oldDescription;
    }

    public int getOldInputPrice() {
        return oldInputPrice;
    }

    public void setOldInputPrice(int oldInputPrice) {
        this.oldInputPrice = oldInputPrice;
    }

    public int getOldOutputPrice() {
        return oldOutputPrice;
    }

    public void setOldOutputPrice(int oldOutputPrice) {
        this.oldOutputPrice = oldOutputPrice;
    }

    public int getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(int oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public long getAddedDay() {
        return AddedDay;
    }

    public void setAddedDay(long addedDay) {
        AddedDay = addedDay;
    }

    public long getEditedDay() {
        return EditedDay;
    }

    public void setEditedDay(long editedDay) {
        EditedDay = editedDay;
    }
}
