package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Table;

/**
 * 地区表 districts
 *
 * @author ruoyi
 * @date 2018-12-19
 */
@Data
@Table(name = "sys_districts")
public class Districts extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Integer id;

    /**
     * 上级编号
     */
    private Integer pid;

    /**
     * 层级
     */
    private Integer deep;

    /**
     * 名称
     */
    private String name;

    /**
     * 上级名称
     */
    private String pname;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 拼音缩写
     */
    private String pinyinShor;

    /**
     * 扩展名
     */
    private String extName;

    /**
     * 操作人
     */
    private String operator;

}
