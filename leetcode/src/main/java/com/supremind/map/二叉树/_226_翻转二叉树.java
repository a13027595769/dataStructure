package com.supremind.map.二叉树;

public class _226_翻转二叉树 {
    public static class Node<E>{
        E element;
        BinarySearchTree.Node<E> left;
        BinarySearchTree.Node<E> right;
        BinarySearchTree.Node<E> parent;
        public Node(E element, BinarySearchTree.Node<E> parent){
            this.element = element;
            this.parent = parent;
        }
        public boolean isLeaf(){
            return left == null && right == null;
        }
        public boolean hasTwoChildren(){
            return left != null && right != null;
        }
    }
    //前序遍历
    public TreeNode inverseTree(TreeNode root){
        if(root == null) return root;

        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        inverseTree(root.left);
        inverseTree(root.right);

        return root;
    }
    //后序遍历
    public TreeNode inverseTree2(TreeNode root){
        if(root == null) return root;


        inverseTree2(root.left);
        inverseTree2(root.right);
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        return root;
    }

    //中序遍历
    public TreeNode inverseTree3(TreeNode root){
        if(root == null) return root;


        inverseTree3(root.left);
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        //这里的左右已经交换顺序了，本来应该是right，所以变成了left
        inverseTree3(root.left);

        return root;
    }

    static void test10(){

        Integer data[] = new Integer[]{
                7,4,9,2,5,8,10
                //7,4,2,1,3,5,9,8,11,10,12
        };
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
        bst.inverseTree2((BinarySearchTree.Node)bst.root());
        //因为是递归调用，所以子树也要递归的将左右进行调换位置
        BinaryTrees.println(bst);
    }

    public static void main(String[] args) {
        test10();

    }
}
