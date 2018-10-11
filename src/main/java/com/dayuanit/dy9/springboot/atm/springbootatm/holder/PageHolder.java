package com.dayuanit.dy9.springboot.atm.springbootatm.holder;

public class PageHolder {

    public static final int prePageNum = 200;
    private int currnetPage = 1;
    private int totalPage;
    private int offset;
    private int totalData;
    private Object data;

    private PageHolder(int currnetPage, int totalData) {
        this.currnetPage = currnetPage;
        this.totalData = totalData;
    }

    public static PageHolder builder(int currnetPage, int totalData) {
        PageHolder pageHolder = new PageHolder(currnetPage, totalData);
        pageHolder.offset();
        pageHolder.totalPage();
        return pageHolder;
    }

    public void offset() {
        offset = (currnetPage - 1) * prePageNum;
    }

    public void totalPage() {
        totalPage = totalData % prePageNum == 0 ? totalData / prePageNum : totalData / prePageNum + 1;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public int getCurrnetPage() {
        return this.currnetPage;
    }

}
