package com.ruoyi.system.service;

import com.ruoyi.system.domain.Oss;

import java.util.List;

/**
 * 文件上传 服务层
 *
 * @author zmr
 * @date 2019-05-16
 */
public interface IOssService {
    /**
     * 查询文件上传信息
     *
     * @param id 文件上传ID
     * @return 文件上传信息
     */
    Oss selectSysOssById(Long id);

    /**
     * 查询文件上传列表
     *
     * @param oss 文件上传信息
     * @return 文件上传集合
     */
    List<Oss> selectSysOssList(Oss oss);

    /**
     * 新增文件上传
     *
     * @param oss 文件上传信息
     * @return 结果
     */
    int insertSysOss(Oss oss);

    /**
     * 修改文件上传
     *
     * @param oss 文件上传信息
     * @return 结果
     */
    int updateSysOss(Oss oss);

    /**
     * 删除文件上传信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysOssByIds(String ids);

}
