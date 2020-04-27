package com.example.hander.pojo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CheckWebParamVo {
    private String[] notNull;
    private boolean checkToken;
    private boolean checkUserName;
}
