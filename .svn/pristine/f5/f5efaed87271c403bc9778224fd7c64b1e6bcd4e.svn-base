package com.icloud.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-10-26 09:33:26
 */
@Data
@TableName("t_sys_whiteip")
public class SysWhiteip implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /*  */
       @TableId(value="id", type= IdType.AUTO)
       private Integer id;
   	   	   /*  */
       @NotEmpty(message = "企业不能为空")
       @TableField("supplier_name")
       private String supplierName;
   	   	   /*  */
       @Pattern(regexp="^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$",message="ip格式不正确")
       @TableField("ipname")
       private String ipname;
   	   	   /*  */
       @NotEmpty(message = "状态不能为空")
       @TableField("status")
       private String status;
   	   	   /*  */
       @TableField("create_time")
       private Date createTime;
   	   	   /*  */
       @TableField("modify_time")
       private Date modifyTime;
   	
}
