package com.dayuanit.dy9.springboot.atm.springbootatm.entity;

import java.util.Date;

public class User {
    private Integer id;

    private String username;

    private String pwd;

    private Date createTiime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public Date getCreateTiime() {
        return createTiime;
    }

    public void setCreateTiime(Date createTiime) {
        this.createTiime = createTiime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}