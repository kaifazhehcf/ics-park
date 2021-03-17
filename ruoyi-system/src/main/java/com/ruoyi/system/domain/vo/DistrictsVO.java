package com.ruoyi.system.domain.vo;

import com.ruoyi.common.utils.tree.TreeNode;
import lombok.Data;

import java.util.List;

/**
 * 地区
 * @author jack
 */
@Data
public class DistrictsVO implements TreeNode<DistrictsVO> {

    /**
     * 编号
     */
    private Long value;

    /**
     * 上级编号
     */
    private Long pid;

    /**
     * 名称
     */
    private String label;

    /**
     * 下级分类
     */
    private List<DistrictsVO> children;

    @Override
    public long getId() {
        return this.value;
    }

    @Override
    public long getParentId() {
        return this.pid;
    }

    @Override
    public List<DistrictsVO> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(List<DistrictsVO> children) {
        this.children = children;
    }
}
