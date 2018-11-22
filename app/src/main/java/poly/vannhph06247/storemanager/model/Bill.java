package poly.vannhph06247.storemanager.model;

public class Bill {
    private String billID;
    private String customerPhone;
    private String oldCustomerPhone;
    private long billDate;
    private long editedDay;

    public Bill(String billID, String customerPhone, String oldCustomerPhone, long billDate, long editedDay) {
        this.billID = billID;
        this.customerPhone = customerPhone;
        this.oldCustomerPhone = oldCustomerPhone;
        this.billDate = billDate;
        this.editedDay = editedDay;
    }

    public Bill() {

    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getOldCustomerPhone() {
        return oldCustomerPhone;
    }

    public void setOldCustomerPhone(String oldCustomerPhone) {
        this.oldCustomerPhone = oldCustomerPhone;
    }

    public long getBillDate() {
        return billDate;
    }

    public void setBillDate(long billDate) {
        this.billDate = billDate;
    }

    public long getEditedDay() {
        return editedDay;
    }

    public void setEditedDay(long editedDay) {
        this.editedDay = editedDay;
    }
}
