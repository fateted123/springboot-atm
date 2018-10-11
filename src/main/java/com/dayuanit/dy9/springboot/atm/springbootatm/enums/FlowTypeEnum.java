package com.dayuanit.dy9.springboot.atm.springbootatm.enums;

public enum  FlowTypeEnum {

    deposit(1, "存款"), draw(2, "取款"), transferOut(3, "转账支出"), transferIn(4, "转账收入");

    private int k;
    private String v;

    private FlowTypeEnum(int k, String v) {
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
