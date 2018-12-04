package poly.project.storemanager.model;

public class Customer {
    private String customerPhone, customerName, customerSex, customerEmail, customerAddress, note,
            oldCustomerName, oldCustomerSex, oldCustomerEmail, oldCustomerAddress, oldNote;
    private int customerAge, oldCustomerAge;
    private long addedDay, editedDay;
    private byte[]imgcustomer,oldImgCustomer;

    public Customer(String customerPhone, String customerName, String customerSex, String customerEmail, String customerAddress, String note, String oldCustomerName, String oldCustomerSex, String oldCustomerEmail, String oldCustomerAddress, String oldNote, int customerAge, int oldCustomerAge, long addedDay, long editedDay, byte[] imgcustomer, byte[] oldImgCustomer) {
        this.customerPhone = customerPhone;
        this.customerName = customerName;
        this.customerSex = customerSex;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.note = note;
        this.oldCustomerName = oldCustomerName;
        this.oldCustomerSex = oldCustomerSex;
        this.oldCustomerEmail = oldCustomerEmail;
        this.oldCustomerAddress = oldCustomerAddress;
        this.oldNote = oldNote;
        this.customerAge = customerAge;
        this.oldCustomerAge = oldCustomerAge;
        this.addedDay = addedDay;
        this.editedDay = editedDay;
        this.imgcustomer = imgcustomer;
        this.oldImgCustomer = oldImgCustomer;
    }

    public byte[] getOldImgCustomer() {
        return oldImgCustomer;
    }

    public void setOldImgCustomer(byte[] oldImgCustomer) {
        this.oldImgCustomer = oldImgCustomer;
    }

    public Customer() {

    }

    public byte[] getImgcustomer() {
        return imgcustomer;
    }

    public void setImgcustomer(byte[] imgcustomer) {
        this.imgcustomer = imgcustomer;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSex() {
        return customerSex;
    }

    public void setCustomerSex(String customerSex) {
        this.customerSex = customerSex;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOldCustomerName() {
        return oldCustomerName;
    }

    public void setOldCustomerName(String oldCustomerName) {
        this.oldCustomerName = oldCustomerName;
    }

    public String getOldCustomerSex() {
        return oldCustomerSex;
    }

    public void setOldCustomerSex(String oldCustomerSex) {
        this.oldCustomerSex = oldCustomerSex;
    }

    public String getOldCustomerEmail() {
        return oldCustomerEmail;
    }

    public void setOldCustomerEmail(String oldCustomerEmail) {
        this.oldCustomerEmail = oldCustomerEmail;
    }

    public String getOldCustomerAddress() {
        return oldCustomerAddress;
    }

    public void setOldCustomerAddress(String oldCustomerAddress) {
        this.oldCustomerAddress = oldCustomerAddress;
    }

    public String getOldNote() {
        return oldNote;
    }

    public void setOldNote(String oldNote) {
        this.oldNote = oldNote;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(int customerAge) {
        this.customerAge = customerAge;
    }

    public int getOldCustomerAge() {
        return oldCustomerAge;
    }

    public void setOldCustomerAge(int oldCustomerAge) {
        this.oldCustomerAge = oldCustomerAge;
    }

    public long getAddedDay() {
        return addedDay;
    }

    public void setAddedDay(long addedDay) {
        this.addedDay = addedDay;
    }

    public long getEditedDay() {
        return editedDay;
    }

    public void setEditedDay(long editedDay) {
        this.editedDay = editedDay;
    }
}
