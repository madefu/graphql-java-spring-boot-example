package com.example.DemoGraphQL.exception;

import lombok.Getter;

public enum BizErrorEnum {

    NotFoundObject(40001,"为找对象"),
    UnKnownException(40400, "未知异常");
    private BizErrorEnum(Integer code , String msg){}

    private BizErrorEnum() {

    }

    @Getter
    private Integer code;
    @Getter
    private String msg;
}
