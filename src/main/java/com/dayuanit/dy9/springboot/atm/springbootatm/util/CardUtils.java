package com.dayuanit.dy9.springboot.atm.springbootatm.util;

import java.util.Random;

public class CardUtils {

    private static final int card_length = 5;

    public static String createCardNum() {
        final Random random = new Random();
        StringBuilder sx = new StringBuilder();

        for (int i = 0; i < card_length; i++) {
            final int number = random.nextInt();
            sx.append(String.valueOf(Math.abs(number % 10)));
        }

        System.out.println(sx.toString());
        return sx.toString();
    }

    public static String formatCard(String cardNum) {
        char first = cardNum.charAt(0);
        char last = cardNum.charAt(cardNum.length() - 1);
        return String.valueOf(first) + "***" + String.valueOf(last);
    }

    public static void main(String[] args) {
        CardUtils.createCardNum();
    }
}
