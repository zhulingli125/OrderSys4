package com.chinasofti.ordersys.vo;



import java.util.Random;

public class Sms {
    private String phoneNumber;
    private String code;
    public static final int MIN = 5;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getCode() {
        int a = new Random().nextInt(10);
        int b = new Random().nextInt(10);
        int c = new Random().nextInt(10);
        int d = new Random().nextInt(10);
        code = a+""+b+""+c+""+d;
        System.out.println(code);
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
