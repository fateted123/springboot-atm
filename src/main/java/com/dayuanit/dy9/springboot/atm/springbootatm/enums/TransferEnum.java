package com.dayuanit.dy9.springboot.atm.springbootatm.enums;

public enum TransferEnum {

    //反面教材  变量不能使用中文
    待处理(0, "待处理"), 转账成功(1, "转账成功"), 转账失败(2, "转账失败")
    ;

    private int k;
    private String v;

    private TransferEnum(int k, String v) {
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
