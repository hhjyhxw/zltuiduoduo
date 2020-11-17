package com.icloud.api.crowb.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class SignVo {
    @NotEmpty(message = "活动编号不能为空")
    private String activityNo;//活动编号
    /* 姓名 */
    @NotEmpty(message = "用户姓名不能为空")
    private String name;
    /* 手机 */
    /* 手机号 */
    @NotEmpty(message = "用户手机号不能为空")
    @Pattern(regexp = "^[1][2,3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String phone;
    /* 是否带同伴，停用标志 0不带，1带同伴(默认不带) */
    private String takePartner;
    /* 是否有赞助意向，停用标志 0否，1是 */
    private String suportintention;
    /* 单位名称 */
    @NotNull(message = "单位名称")
    private String unitName;
    /* 职务名称 */
    @NotNull(message = "职务名称")
    private String officeName;
    /* 携带同伴人数 */
    private Integer takeNum;

}
