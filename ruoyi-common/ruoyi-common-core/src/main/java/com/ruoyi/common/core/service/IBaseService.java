package com.ruoyi.common.core.service;

import java.util.List;

/**
 * Entity基类
 *
 * @author ruoyi
 */
public interface IBaseService<T> {

    /**
     * 查找实体对象
     *
     * @param property 属性名称
     * @param value    属性值
     * @return 实体对象，若不存在则返回null
     */
    T find(String property, Object value);

    /**
     * 查找实体对象集合
     *
     * @param property 属性名称
     * @param value    属性值
     * @return 实体对象，若不存在则返回null
     */
    List<T> findList(String property, Object value);

    /**
     * 查找所有实体对象集合
     *
     * @return 所有实体对象集合
     */
    List<T> findAll();

    /**
     * 查找实体对象集合
     *
     * @param ids
     *            ID
     * @return 实体对象集合
     */
    List<T> findList(Long[] ids);

    /**
     * 判断是否存在
     *
     * @param property 属性名称
     * @param value    属性值
     * @return 是否存在
     */
    boolean exists(String property, Object value);

    /**
     * 判断是否唯一
     *
     * @param id       ID
     * @param property 属性名称
     * @param value    属性值
     * @return 是否唯一
     */
    boolean unique(Long id, String property, Object value);

    /**
     * 保存实体对象
     *
     * @param entity
     *            实体对象
     * @return 实体对象
     */
    int save(T entity);

    /**
     * 更新实体对象
     *
     * @param entity
     *            实体对象
     * @return 实体对象
     */
    int update(T entity);

    /**
     * 删除实体对象
     *
     * @param id
     *            ID
     */
    int delete(Long id);

    /**
     * 删除实体对象
     *
     * @param ids
     *            ID
     */
    int delete(String ids);
}
