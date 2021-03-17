package com.ruoyi.common.core.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.ruoyi.common.core.dao.BaseMapper;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.service.IBaseService;
import com.ruoyi.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Service基类
 *
 * @author ruoyi
 */
public class IBaseServiceImpl<T extends BaseEntity<T>> implements IBaseService<T> {

    /**
     * 实体类类型
     */
    private Class<T> entityClass;

    /**
     * baseMapper
     */
    private BaseMapper<T> baseMapper;

    /**
     * 注入baseMapper
     */
    @Autowired
    protected void setBaseMapper(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    /**
     * 构造方法
     */
    public IBaseServiceImpl() {
        ResolvableType resolvableType = ResolvableType.forClass(getClass());
        entityClass = (Class<T>) resolvableType.as(IBaseServiceImpl.class).getGeneric().resolve();
    }

    /**
     * 查找实体对象
     *
     * @param property 属性名称
     * @param value    属性值
     * @return 实体对象，若不存在则返回null
     */
    @Override
    public T find(String property, Object value) {
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(property) && value != null) {
            criteria.andEqualTo(property, value);
        }
        return baseMapper.selectOneByExample(example);
    }

    /**
     * 查找实体对象集合
     *
     * @param property 属性名称
     * @param value    属性值
     * @return 实体对象，若不存在则返回null
     */
    @Override
    public List<T> findList(String property, Object value) {
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(property) && value != null) {
            criteria.andEqualTo(property, value);
        }
        return baseMapper.selectByExample(example);
    }

    /**
     * 查找所有实体对象集合
     *
     * @return 所有实体对象集合
     */
    @Override
    public List<T> findAll() {
        Example example = new Example(entityClass);
        return baseMapper.selectByExample(example);
    }

    /**
     * 查找实体对象集合
     *
     * @param ids ID
     * @return 实体对象集合
     */
    @Override
    public List<T> findList(Long[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        Example example = new Example(entityClass);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        return baseMapper.selectByExample(example);
    }

    /**
     * 判断是否存在
     *
     * @param property 属性名称
     * @param value    属性值
     * @return 是否存在
     */
    @Override
    public boolean exists(String property, Object value) {
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(property) && value != null) {
            criteria.andEqualTo(property, value);
        }
        return baseMapper.selectCountByExample(example) > 0;
    }

    /**
     * 判断是否唯一
     *
     * @param id       ID
     * @param property 属性名称
     * @param value    属性值
     * @return 是否唯一
     */
    @Override
    public boolean unique(Long id, String property, Object value) {
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(property) && value != null) {
            criteria.andEqualTo(property, value);
        }
        if (id != null) {
            criteria.andNotEqualTo("id", id);
        }
        return baseMapper.selectCountByExample(example) > 0;
    }

    /**
     * 保存实体对象
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(T entity) {
        Assert.notNull(entity, "[Assertion failed] - entity is required; it must not be null");

        entity.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(entity);
    }

    /**
     * 更新实体对象
     *
     * @param entity 实体对象
     * @return 实体对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(T entity) {
        Assert.notNull(entity, "[Assertion failed] - entity is required; it must not be null");

        entity.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * 删除实体对象
     *
     * @param id ID
     */
    @Override
    public int delete(Long id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除实体对象
     *
     * @param ids ID
     */
    @Override
    @Transactional
    public int delete(String ids) {
        return baseMapper.deleteByIds(ids);
    }
}
