package com.dayuanit.dy9.springboot.atm.springbootatm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDTO {
    private int id;
    private String cardNum;
    private String balance;
}
