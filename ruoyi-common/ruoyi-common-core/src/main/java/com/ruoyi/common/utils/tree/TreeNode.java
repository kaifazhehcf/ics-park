package com.ruoyi.common.utils.tree;

import java.util.List;

/**
 *
 * @author wangrenhui
 * @date 14-4-12
 */
public interface TreeNode<T> {

    /**
     * 获取ID
     * @return
     */
    long getId();

    /**
     * 获取父ID
     * @return
     */
    long getParentId();

    /**
     * 获取子节点
     * @return
     */
    List<T> getChildren();

    /**
     * 设置子节点
     * @param children
     */
    void setChildren(List<T> children);

}