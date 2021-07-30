package entity;

import java.util.Date;

public class UserToken {

    private String series;
    private String username;
    private String token;
    private Date createTime;
    private Date lastUsedTime;
    private boolean valid;

    public UserToken(String series, String username, String token, Date createTime, Date lastUsedTime, boolean valid) {
        this.series = series;
        this.username = username;
        this.token = token;
        this.createTime = createTime;
        this.lastUsedTime = lastUsedTime;
        this.valid = valid;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(Date lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
