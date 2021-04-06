package com.ruoyi.business.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 政策管理对象 ics_policy
 *
 * @author zzm
 * @date 2020-10-29
 */
@Data
@Table(name = "ics_policy")
public class Policy extends BaseEntity<Policy>
{
    private static final long serialVersionUID = 1L;

    /** 政策ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 政策名称 */
    @NotBlank(message = "政策名称不能为空")
    @Length(max = 50, message = "名称不能超过50个字符")
    private String name;

    /** 上传详情 */
    private String content;

    /** 是否上架 */
    private Boolean isMarketable;

    /** 删除标志（0代表存在 1代表删除） */
    private Boolean delFlag;

    /** 园区id */
    private Long parkId;

    /** 上架时间 */
    private Date marketableTime;


}