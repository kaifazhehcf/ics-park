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
 * 党建管理对象 ics_dj
 *
 * @author zzm
 * @date 2020-10-27
 */
@Data
@Table(name = "ics_dj")
public class Dj extends BaseEntity<Dj> {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 党建名称
     */
    @NotBlank(message = "党建名称不能为空")
    @Length(max = 50, message = "名称不能超过50个字符")
    private String djName;

    /**
     * 上传详情
     */
    private String content;

    /**
     * 是否上架
     */
    private Boolean isMarketable;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private Boolean delFlag;

    /**
     * 上架时间
     */
    private Date marketableTime;

}