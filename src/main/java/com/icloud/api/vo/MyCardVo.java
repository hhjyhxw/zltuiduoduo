package com.icloud.api.vo;

import lombok.Data;

@Data
public class MyCardVo {
    private String url;//推广名片
    private Long expiredTime;//名片失效时间
}
