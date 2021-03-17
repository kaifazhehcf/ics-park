package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Oss;
import com.ruoyi.system.mapper.OssMapper;
import com.ruoyi.system.service.IOssService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

/**
 * 文件上传 服务层实现
 *
 * @author zmr
 * @date 2019-05-16
 */
@Service
public class OssServiceImpl implements IOssService {
    @Autowired
    private OssMapper ossMapper;

    /**
     * 查询文件上传信息
     *
     * @param id 文件上传ID
     * @return 文件上传信息
     */
    @Override
    public Oss selectSysOssById(Long id) {
        return ossMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询文件上传列表
     *
     * @param oss 文件上传信息
     * @return 文件上传集合
     */
    @Override
    public List<Oss> selectSysOssList(Oss oss) {
        Example example = new Example(Oss.class);
        Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(oss.getFileName())) {
            criteria.andLike("fileName", "%" + oss.getFileName() + "%");
        }
        if (StringUtils.isNotBlank(oss.getFileSuffix())) {
            criteria.andEqualTo("fileSuffix", oss.getFileSuffix());
        }
        if (StringUtils.isNotBlank(oss.getCreateBy())) {
            criteria.andLike("createBy", oss.getCreateBy());
        }
        return ossMapper.selectByExample(example);
    }

    /**
     * 新增文件上传
     *
     * @param oss 文件上传信息
     * @return 结果
     */
    @Override
    public int insertSysOss(Oss oss) {
        return ossMapper.insertSelective(oss);
    }

    /**
     * 修改文件上传
     *
     * @param oss 文件上传信息
     * @return 结果
     */
    @Override
    public int updateSysOss(Oss oss) {
        return ossMapper.updateByPrimaryKeySelective(oss);
    }

    /**
     * 删除文件上传对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysOssByIds(String ids) {
        return ossMapper.deleteByIds(ids);
    }

}
