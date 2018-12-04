package poly.project.storemanager.model;

public class ProductType{
    private String ProductTypeID;
    private String ProductTypeName;
    private String ProductTypeDes;
    private String OldProductTypeName;
    private String OldProductTypeDes;
    private long AddedDay;
    private long EditedDay;

    public ProductType(String productTypeID, String productTypeName, String productTypeDes, String oldProductTypeName, String oldProductTypeDes, long addedDay, long editedDay) {
        ProductTypeID = productTypeID;
        ProductTypeName = productTypeName;
        ProductTypeDes = productTypeDes;
        OldProductTypeName = oldProductTypeName;
        OldProductTypeDes = oldProductTypeDes;
        AddedDay = addedDay;
        EditedDay = editedDay;
    }

    public ProductType() {

    }

    public String getProductTypeID() {
        return ProductTypeID;
    }

    public void setProductTypeID(String productTypeID) {
        ProductTypeID = productTypeID;
    }

    public String getProductTypeName() {
        return ProductTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        ProductTypeName = productTypeName;
    }

    public String getProductTypeDes() {
        return ProductTypeDes;
    }

    public void setProductTypeDes(String productTypeDes) {
        ProductTypeDes = productTypeDes;
    }

    public String getOldProductTypeName() {
        return OldProductTypeName;
    }

    public void setOldProductTypeName(String oldProductTypeName) {
        OldProductTypeName = oldProductTypeName;
    }

    public String getOldProductTypeDes() {
        return OldProductTypeDes;
    }

    public void setOldProductTypeDes(String oldProductTypeDes) {
        OldProductTypeDes = oldProductTypeDes;
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
