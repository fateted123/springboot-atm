package com.dayuanit.dy9.springboot.atm.springbootatm.entity;

import java.util.Date;

public class Transfer {
    private Integer id;

    private Integer userId;

    private String outCardNum;

    private String inCardNum;

    private String amount;

    private Integer status;

    private Date createTime;

    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOutCardNum() {
        return outCardNum;
    }

    public void setOutCardNum(String outCardNum) {
        this.outCardNum = outCardNum == null ? null : outCardNum.trim();
    }

    public String getInCardNum() {
        return inCardNum;
    }

    public void setInCardNum(String inCardNum) {
        this.inCardNum = inCardNum == null ? null : inCardNum.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}