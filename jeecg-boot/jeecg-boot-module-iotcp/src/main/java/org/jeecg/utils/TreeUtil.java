package org.jeecg.utils;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

    /**
     * 列表转换为树
     *
     * @param nodeList
     * @return
     */
    public static <T extends TreeNode<T>> List<T> listToTree(List<T> nodeList) {
        List<T> tree = new ArrayList<T>();
        for (T node : nodeList) {
            if (node.isTrunk()) {
                tree.add(findChildren(node, nodeList));
            }
        }
        return tree;
    }

    /**
     * 递归查找子节点
     *
     * @param nodeList
     * @return
     */
    public static <T extends TreeNode<T>> T findChildren(T node, List<T> nodeList) {
        for (T it : nodeList) {
            if (node.isParentOf(it)) {
                if (node.getChildren() == null) {
                    node.setChildren(new ArrayList<T>());
                }
                node.getChildren().add(findChildren(it, nodeList));
            }
        }
        return node;
    }

    /**
     * 两个对象是否相等
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean isEquals(Object source, Object target) {
        if (source == null && target == null) {
            return true;
        }
        if (source == null || target == null) {
            return false;
        }
        return source.equals(target);
    }

}

