package com.itheima.mobilesafe.classDemo;

/**
 * Created by ${wbb} on 2016/4/29.
 */
public class BlackNumberInfo {
    private  String  number;
    /**
     * 黑名单拦截模式
     *
     */
    private String mode;


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "number='" + number + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}
