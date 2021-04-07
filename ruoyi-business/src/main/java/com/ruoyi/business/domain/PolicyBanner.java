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
 * 政策banner对象 ics_policy_banner
 *
 * @author zzm
 * @date 2020-10-29
 */
@Data
@Table(name = "ics_policy_banner")
public class PolicyBanner extends BaseEntity<PolicyBanner> {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * banner名称
     */
    @NotBlank(message = "banner名称不能为空")
    @Length(max = 6, message = "banner名称不能超过6个字符")
    private String name;

    /**
     * banner简介（可选）
     */
    @Length(max = 50, message = "banner简介不能超过50个字符")
    private String bannerDesc;

    /**
     * 点击次数
     */
    private Long hits;

    /**
     * 是否上架
     */
    private Boolean isMarketable;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private Boolean delFlag;

    /**
     * 园区id
     */
    private Long parkId;

    /**
     * 上架时间
     */
    private Date marketableTime;

    /**
     * 落地页URI
     */
    private String uri;

    /**
     * 图片路径
     */
    private String bannerImg;

}