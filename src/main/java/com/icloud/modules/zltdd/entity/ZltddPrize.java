package com.icloud.modules.zltdd.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 奖品表
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 */
@Data
@TableName("t_zltdd_prize")
public class ZltddPrize implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 奖品名称 */
       @NotEmpty(message = "奖品名称不能为空")
       @TableField("prize_name")
       private String prizeName;
   	   	   /* 奖品代码 */
       @NotEmpty(message = "奖品代码不能为空")
       @TableField("prize_code")
       private String prizeCode;
   	   	   /* 业务处理器 */
       @TableField("bissiness_zlass")
       private String bissinessZlass;
   	   	   /* 状态 */
       @TableField("status")
       private String status;
   	   	   /* 创建时间 */
       @TableField("create_time")
       private Date createTime;
   	   	   /* 创建人 */
       @TableField("create_man")
       private String createMan;
   	   	   /* 修改时间 */
       @TableField("modify_time")
       private Date modifyTime;
   	   	   /* 修改人 */
       @TableField("modify_man")
       private String modifyMan;
   	
}
