package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lulu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ParamException extends RuntimeException {
    private int code;
    private String msg;

    public ParamException(String msg) {
        this.msg = msg;
        code = -1;
    }
}
