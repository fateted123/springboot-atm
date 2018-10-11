package com.dayuanit.dy9.springboot.atm.springbootatm.enums;

public enum  CardStatusEnum {

    enable(1, "可用"), unenable(2, "冻结"), delete(3, "删除");

    private int k;
    private String v;

    private CardStatusEnum(int k, String v) {
        this.k = k;
        this.v = v;
    }

    public int getK() {
        return k;
    }

    public String getV() {
        return v;
    }
}
