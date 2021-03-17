package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

/**
 * 字典类型表 sys_dict_type
 *
 * @author ruoyi
 */
@Data
@Table(name = "sys_dict_type")
public class DictType extends BaseEntity<BaseEntity> {
    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    private Long dictId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
