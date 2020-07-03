package org.jeecg.utils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class TreeTest {

    /**
     * 测试用例
     */
    // @Test
    public static void main(String[] args) {

        Menu treeNode1 = new Menu("1", "广州", null);
        Menu treeNode2 = new Menu("2", "深圳", null);

        Menu treeNode3 = new Menu("3", "天河区", treeNode1.getId());
        Menu treeNode4 = new Menu("4", "越秀区", treeNode1.getId());
        Menu treeNode5 = new Menu("5", "黄埔区", treeNode1.getId());
        Menu treeNode6 = new Menu("6", "石牌", treeNode3.getId());
        Menu treeNode7 = new Menu("7", "百脑汇", treeNode6.getId());

        Menu treeNode8 = new Menu("8", "南山区", treeNode2.getId());
        Menu treeNode9 = new Menu("9", "宝安区", treeNode2.getId());
        Menu treeNode10 = new Menu("10", "科技园", treeNode8.getId());

        List<Menu> list = new ArrayList<Menu>();
        list.add(treeNode1);
        list.add(treeNode2);
        list.add(treeNode3);
        list.add(treeNode4);
        list.add(treeNode5);
        list.add(treeNode6);
        list.add(treeNode7);
        list.add(treeNode8);
        list.add(treeNode9);
        list.add(treeNode10);

        List<Menu> trees = TreeUtil.listToTree(list);
        System.out.println(JSON.toJSON(trees));

    }

    static class Menu extends TreeNode<Menu> {
        String id;
        String parentId;
        String name;
        List<Menu> children;

        public Menu(String id, String name, String parentId) {
            this.id = id;
            this.name = name;
            this.parentId = parentId;
        }

        @Override
        public String getParentId() {
            return parentId;
        }

        @Override
        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }

        @Override
        public List<Menu> getChildren() {
            return this.children;
        }

        @Override
        public void setChildren(List<Menu> children) {
            this.children = children;
        }

    }

}

