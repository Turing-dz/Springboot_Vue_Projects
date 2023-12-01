package com.example.common.enums;

public enum OrderEnum {
    STATUS_CHECKING("待入住"),
    STATUS_IN("已入住"),
    STATUS_OUT("已退房"),
    ;

    public String status;

    OrderEnum(String status) {
        this.status = status;
    }
}
