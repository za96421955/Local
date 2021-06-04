package com.chen.local.learn.dataStructureAndAlgorithm.search.tree;

/**
 * 二叉树
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2021/5/27
 */
public class BinaryTree {

    private int[] completeBTree() {
        /**
         *          5
         *      3        7
         *    2   4   6     9
         *  1             8
         */
        int[] datas = new int[15];
        datas[1] = 5;       // 1N = 1
        datas[2] = 3;       // 2N = 2 * 1N
        datas[3] = 7;       // 2N + 1
        datas[4] = 2;       // 3N = 2 * 2N
        datas[5] = 4;       // 3N + 1
        datas[6] = 6;
        datas[7] = 9;
        datas[8] = 1;
        datas[14] = 8;
        return datas;
    }

    private BinaryTreeNode linkedBTree() {
        /**
         *          5
         *      3        7
         *    2   4   6     9
         *  1             8
         */
        BinaryTreeNode root = new BinaryTreeNode(5);
        BinaryTreeNode node3 = new BinaryTreeNode(3);
        BinaryTreeNode node7 = new BinaryTreeNode(7);
        root.setLeft(node3);
        root.setRight(node7);

        BinaryTreeNode node2 = new BinaryTreeNode(2);
        BinaryTreeNode node4 = new BinaryTreeNode(4);
        node3.setLeft(node2);
        node3.setRight(node4);

        BinaryTreeNode node1 = new BinaryTreeNode(1);
        node2.setLeft(node1);

        BinaryTreeNode node6 = new BinaryTreeNode(6);
        BinaryTreeNode node9 = new BinaryTreeNode(9);
        node7.setLeft(node6);
        node7.setRight(node9);

        BinaryTreeNode node8 = new BinaryTreeNode(8);
        node9.setLeft(node8);
        return root;
    }

    /**
     * @description 前序遍历
     * <p>自己 > 左 > 右</p>
     *
     * @author 陈晨
     * @date 2021/5/27 15:30
     */
    private BinaryTreeNode beforeSearch(BinaryTreeNode node, int target) {
        if (node == null) {
            return null;
        }
        System.out.printf("%s == %s%n", node.getValue(), target);
        if (node.getValue() == target) {
            return node;
        }
        System.out.printf("%s > %s%n", node.getValue(), target);
        if (node.getValue() > target) {
            return this.beforeSearch(node.getLeft(), target);
        }
        System.out.printf("%s < %s%n", node.getValue(), target);
        return this.beforeSearch(node.getRight(), target);
    }

    /**
     * @description 中序遍历
     * <p>左 > 自己 > 右</p>
     *
     * @author 陈晨
     * @date 2021/5/27 15:30
     */
    private BinaryTreeNode midSearch(BinaryTreeNode node, int target) {
        if (node == null) {
            return null;
        }
        System.out.printf("%s > %s%n", node.getValue(), target);
        if (node.getValue() > target) {
            return this.midSearch(node.getLeft(), target);
        }
        System.out.printf("%s == %s%n", node.getValue(), target);
        if (node.getValue() == target) {
            return node;
        }
        System.out.printf("%s < %s%n", node.getValue(), target);
        return this.midSearch(node.getRight(), target);
    }

    /**
     * @description 后序遍历
     * <p>左 > 右 > 自己</p>
     *
     * @author 陈晨
     * @date 2021/5/27 15:30
     */
    private BinaryTreeNode rightSearch(BinaryTreeNode node, int target) {
        if (node == null) {
            return null;
        }
        System.out.printf("%s > %s%n", node.getValue(), target);
        if (node.getValue() > target) {
            return this.rightSearch(node.getLeft(), target);
        }
        System.out.printf("%s < %s%n", node.getValue(), target);
        if (node.getValue() < target) {
            return this.rightSearch(node.getRight(), target);
        }
        System.out.printf("%s == %s%n", node.getValue(), target);
        return node;
    }

    public static void main(String[] args) {
        BinaryTree btree = new BinaryTree();
        BinaryTreeNode root = btree.linkedBTree();
        System.out.println("==== before search ====");
        btree.beforeSearch(root, 9);

        System.out.println("\n==== mid search ====");
        btree.midSearch(root, 9);

        System.out.println("\n==== right search ====");
        btree.rightSearch(root, 9);
    }

}


