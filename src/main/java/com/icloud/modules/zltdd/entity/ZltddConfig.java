package com.icloud.modules.zltdd.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 活动配置表
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-11-02 14:35:33
 */
@Data
@TableName("t_zltdd_config")
public class ZltddConfig implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 活动名称 */
       @NotEmpty(message = "活动名称不能为空")
       @TableField("activity_name")
       private String activityName;
   	   	   /* 活动状态 */
       @TableField("status")
       private String status;
   	   	   /* 活动开启时间 */
       @TableField("start_time")
       private Date startTime;
   	   	   /* 活动结束时间 */
       @TableField("end_time")
       private Date endTime;
   	   	   /* 活动创建时间 */
       @TableField("create_time")
       private Date createTime;
   	   	   /* 活动修改时间 */
       @TableField("modify_time")
       private Date modifyTime;
   	   	   /* 活动创建人 */
       @TableField("create_man")
       private String createMan;
   	   	   /* 活动修改人 */
       @TableField("mofity_man")
       private String mofityMan;
   	   	   /* 奖品id */
       @NotNull(message = "活动奖品不能为空")
       @TableField("prize_id")
       private Long prizeId;
   	   	   /* 最大发展人数 */
       @NotNull(message = "最大发展不能为空")
       @TableField("max_num")
       private Integer maxNum;
        /*奖品*/
        @TableField(exist = false)
        private ZltddPrize prize;

}
