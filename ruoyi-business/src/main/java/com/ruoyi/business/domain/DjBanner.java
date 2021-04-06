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
import java.util.List;

/**
 * 党建banner对象 ics_dj_banner
 *
 * @author zzm
 * @date 2020-10-27
 */
@Data
@Table(name = "ics_dj_banner")
public class DjBanner extends BaseEntity<DjBanner>
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** banner名称 */
    @NotBlank(message = "banner名称不能为空")
    @Length(max = 6, message = "banner名称不能超过6个字符")
    private String bannerName;

    /** banner简介（可选） */
    @Length(max = 50, message = "banner简介不能超过50个字符")
    private String bannerDesc;

    /** 上传banner图标 */
    private String bannerImg;

    /** $column.columnComment */
    private String url;

    /** 点击次数 */
    private Long hits;

    /** 是否上架 */
    private Boolean isMarketable;

    /** 是否置顶 */
    private Long isTop;

    /** 删除标志（0代表存在 1代表删除） */
    private Boolean delFlag;

    /** 上架时间 */
    private Date marketableTime;


}
