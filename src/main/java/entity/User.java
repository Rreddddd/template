package entity;

import java.util.HashMap;
import java.util.Map;

public class User extends Member {

    private String account;
    private String password;
    private String phone;
    private String email;

    private static final Map<String, User> cache = new HashMap<>();

    public static User get(String account) {
        return null;
    }

    public User() {
        this.type = MemberType.USER.getType();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
