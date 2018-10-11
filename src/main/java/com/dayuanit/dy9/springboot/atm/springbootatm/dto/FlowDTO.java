package com.dayuanit.dy9.springboot.atm.springbootatm.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlowDTO {

    private String cardNum;
    private String amount;
    private String flowDesc;
    private String createTime;

}
