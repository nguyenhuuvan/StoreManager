package poly.vannhph06247.storemanager.model;

public class User {
    private final String Username;
    private final String Password;
    private final String AdminName;
    private final String AdminPhone;
    private final String AdminEmail;
    private final int AdminAge;
    public User(String username, String password, String adminName, String adminPhone, String adminEmail, int adminAge) {
        Username = username;
        Password = password;
        AdminName = adminName;
        AdminPhone = adminPhone;
        AdminEmail = adminEmail;
        AdminAge = adminAge;
    }


    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getAdminName() {
        return AdminName;
    }

    public String getAdminPhone() {
        return AdminPhone;
    }

    public String getAdminEmail() {
        return AdminEmail;
    }

    public int getAdminAge() {
        return AdminAge;
    }

}
