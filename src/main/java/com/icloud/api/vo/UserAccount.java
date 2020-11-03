package com.icloud.api.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserAccount {
    @NotEmpty(message = "客户编号不能为空")
    private String licenese;//许可账号
    @NotEmpty(message = "手机号不能为空")
    private String contactPhone;//手机号
}
