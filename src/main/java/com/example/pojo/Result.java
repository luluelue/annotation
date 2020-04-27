package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private int code;
    private String msg;
    private Object object;

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Object object) {
        this.object = object;
        this.code = 0;
        this.msg = "success";
    }
}
