package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.enums.IEnum;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 序列号对象 sn
 *
 * @author ruoyi
 * @date 2020-05-12
 */
@Data
@Table(name = "sys_sn")
public class Sn {

    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    public enum Type implements IEnum<Integer> {

        /**
         * 报修单
         */
        REPAIR("报修单", 0);

        private String name;
        private int value;

        Type(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }
    }

    /**
     * ID
     */
    @Id
    private Long id;

    /**
     * 类型
     */
    private Type type;

    /**
     * 末值
     */
    private Long lastValue;

    /**
     * 版本
     */
    private Long version;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
