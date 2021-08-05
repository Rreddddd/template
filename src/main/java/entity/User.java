package entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class User {

    private Integer id;
    private String account;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String headImg;
    private Date createTime;
    private boolean freeze;
    private Position position;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public Position getPosition() {
        return position;
    }

    public Integer getPositionId() {
        return position == null ? null : position.getId();
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public User copy() {
        User newUser = new User();
        newUser.setId(id);
        newUser.setAccount(account);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setPhone(phone);
        newUser.setEmail(email);
        newUser.setHeadImg(headImg);
        newUser.setCreateTime(createTime);
        newUser.setFreeze(freeze);
        if (position != null) {
            Position newPosition = new Position();
            newPosition.setId(position.getId());
            newPosition.setName(position.getName());
            newPosition.setVisible(position.isVisible());
            newUser.setPosition(newPosition);
        }
        return newUser;
    }
}
