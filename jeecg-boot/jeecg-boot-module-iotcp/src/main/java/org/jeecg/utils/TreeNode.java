package org.jeecg.utils;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 树节点
 *
 * @param <T>
 * @author gaofu
 */
public abstract class TreeNode<T extends TreeNode<T>> {

    /**
     * 判断是否是主枝干
     *
     * @return
     */
    public boolean isTrunk() {
        return (getParentId() == null) || StringUtils.isEmpty(getParentId());
    }

    /**
     * 判断本节点是否是node的父节点
     *
     * @param node
     * @return
     */
    public boolean isParentOf(T node) {
        if (node != null) {
            return TreeUtil.isEquals(node.getParentId(), this.getId());
        }
        return false;
    }

    /**
     * 判断本节点是否是node的孩子节点
     *
     * @param node
     * @return
     */
    public boolean isChildrenOf(T node) {
        if (node == null) {
            if (this.getParentId() == null) {
                return true;
            }
        } else {
            return TreeUtil.isEquals(node.getId(), this.getParentId());
        }
        return false;
    }

    public abstract String getParentId();

    public abstract void setParentId(String parentId);

    public abstract String getId();

    public abstract void setId(String id);

    public abstract List<T> getChildren();

    public abstract void setChildren(List<T> children);

}

