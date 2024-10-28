package com.group1.gosports_jojo.service.impl;

import org.springframework.stereotype.Component;

@Component
public class AuthCode {

    public String returnAuthCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 8; i++) {
            int condition = (int) (Math.random() * 3) + 1;
            switch (condition) {
                case 1:
                    char c1 = (char) ((int) (Math.random() * 26) + 65);//ASCII"英文大寫字母"
                    sb.append(c1);
                    break;
                case 2:
                    char c2 = (char) ((int) (Math.random() * 26) + 97);//"寫小寫英文字母"
                    sb.append(c2);
                    break;
                case 3:
                    sb.append((int) (Math.random() * 10));//"0-9之間的整數"
            }
        }
        return sb.toString();
    }
}
