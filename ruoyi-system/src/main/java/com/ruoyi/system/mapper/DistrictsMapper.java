package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Districts;
import com.ruoyi.system.domain.vo.DistrictsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 地区 数据层
 *
 * @author ruoyi
 * @date 2018-12-19
 */
@Mapper
public interface DistrictsMapper {
    /**
     * 查询地区信息
     *
     * @param id 地区ID
     * @return 地区信息
     */
    Districts selectDistrictsById(Integer id);

    /**
     * 查询地区列表
     *
     * @return 地区集合
     */
    List<DistrictsVO> selectDistrictsVOList();

    /**
     * 查询地区列表
     *
     * @param districts 地区信息
     * @return 地区集合
     */
    List<Districts> selectDistrictsList(Districts districts);

    /**
     * 查询地区列表
     *
     * @param ids 地区信息
     * @return 地区集合
     */
    List<Districts> selectDistrictsByIds(List<Integer> ids);


    /**
     * 新增地区
     *
     * @param districts 地区信息
     * @return 结果
     */
    int insertDistricts(Districts districts);

    /**
     * 修改地区
     *
     * @param districts 地区信息
     * @return 结果
     */
    int updateDistricts(Districts districts);

    /**
     * 删除地区
     *
     * @param id 地区ID
     * @return 结果
     */
    int deleteDistrictsById(Integer id);

    /**
     * 批量删除地区
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDistrictsByIds(String[] ids);

}