package com.ruoyi.system.service;


import com.ruoyi.system.domain.Sn;

/**
 * 序列号Service接口
 * 
 * @author ruoyi
 * @date 2020-05-12
 */
public interface ISnService {

    /**
     * 生成序列号
     *
     * @param type
     *            类型
     * @return 序列号
     */
    String generate(Sn.Type type);
}
