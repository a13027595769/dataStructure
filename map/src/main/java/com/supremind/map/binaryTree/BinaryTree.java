package com.supremind.map.binaryTree;


import com.supremind.map.asserta.printer.BinaryTreeInfo;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree<E>  implements BinaryTreeInfo {
    protected int size;
    protected Node<E> root;

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }
    public void clear(){
        root = null;
        size = 0;
    }

    /**
     * 前序遍历
     */
    public void preorderTraversal(Visitor<E> visitor){
        if(visitor == null)return;
        preorderTraversal(root,visitor);
    }
    private void preorderTraversal(Node<E> node, Visitor<E> visitor){
        if(node == null || visitor.stop) return;
        visitor.stop = visitor.visit(node.element);
        //System.out.println(node.element);
        preorderTraversal(node.left,visitor);
        preorderTraversal(node.right,visitor);
    }

    /**
     * 中序遍历
     */
    public void inorderTraversal(Visitor<E> visitor){
        if(visitor == null)return;
        inorderTraversal(root,visitor);
    }
    private void inorderTraversal(Node<E> node, Visitor<E> visitor){
        if(node == null || visitor.stop)return;
        inorderTraversal(node.left,visitor);
        if(visitor.stop)return;
        visitor.stop = visitor.visit(node.element);
        //  System.out.println(node.element);
        inorderTraversal(node.right,visitor);
    }
    /**
     * 后序遍历
     */
    public void postorderTraversal(Visitor<E> visitor){
        if(visitor == null)return;
        postorderTraversal(root,visitor);
    }
    private void postorderTraversal(Node<E> node, Visitor<E> visitor){
        if(node == null || visitor.stop)return;
        inorderTraversal(node.left,visitor);
        inorderTraversal(node.right,visitor);
        //如果已经变成了ture，递归一进来，就是true，直接就给停了，不管是左还是右，但是左右递归出来过后，
        //可能就已经是true了，下面那个东西就不用执行了，所以要判断一下，
        //两个stop是不一样的，一个是停止递归调用，一个是停止业务方法。
        //System.out.println(node.element);
        if(visitor.stop)return;
        visitor.stop = visitor.visit(node.element);
    }

    public void levelOrder(Visitor<E> visitor){
        //不用担心会不会这一层右子树的还没有完，下一层的左右子树就开始了，
        // 因为用的是队列，先进先出，绝对按照层序遍历来的
        if(root == null || visitor == null)return;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()){
            Node<E> node = queue.poll();
            if(visitor.visit(node.element))return;
            // System.out.println(node.element);
            if(node.left != null){
                queue.offer(node.left);
            }
            if(node.right != null){
                queue.offer(node.right);
            }
        }
    }
    public static abstract class Visitor<E> {
        boolean stop;
        public abstract boolean visit(E element);
    }
    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>)node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>)node).right;
    }

//    @Override
//    public Object string(Object node) {
//        Node<E> myNode = (Node<E>) node;
//        String parentString = "null";
//        if(myNode.parent != null){
//            parentString = myNode.parent.element.toString();
//        }
//        return myNode.element + "_p(" + parentString + ")";
//    }
        @Override
        public Object string(Object node) {
           return node;
        }

    //前驱结点
    protected Node<E> predecessor(Node<E> node){
        if(node == null) return null;
        //前驱节点在左子树当中(left.right.right...)
        Node<E> p = node.left;
        if(p != null){
            while (p.right !=null){
                p = p.right;
            }
            return p;
        }
        //程序来到这里，说明左子树为null，要从祖父节点查找
        while(node.parent != null && node == node.parent.left){
            node = node.parent;
        }
        //1.父节点为null了，那就是没有前驱结点， node.parent == null
        //2.不是父节点的左节点而是右节点 node.parent.right
        return node.parent;
    }

    //后继节点
    protected Node<E> successor(Node<E> node){
        if(node == null) return null;
        //后继节点在右子树当中(right.left.left...)
        Node<E> p = node.right;
        if(p != null){
            while (p.left !=null){
                p = p.left;
            }
            return p;
        }
        //程序来到这里，说明左子树为null，要从祖父节点查找
        while(node.parent != null && node == node.parent.right){
            node = node.parent;
        }
        //1.父节点为null了，那就是没有前驱结点， node.parent == null
        //2.不是父节点的左节点而是右节点 node.parent.left
        return node.parent;
    }




    public int height(){
        if(root == null )return 0;
        //树的高度
        int height = 0;
        //存储着每一层的元素数量
        int levelSize = 1;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()){
            Node<E> node = queue.poll();
            levelSize--;

            if(node.left != null){
                queue.offer(node.left);
            }
            if(node.right != null){
                queue.offer(node.right);
            }

            if(levelSize == 0){
                levelSize = queue.size();
                height++;
            }

        }

        return height;
    }


    public int height2(){
        return height2(root);
    }
    public int height2(Node<E> node){
        if(node == null)return 0;
        return 1 + Math.max(height2(node.left),height2(node.right));
    }
    protected  Node<E> createNode(E element,Node<E> parent){
        return new Node<>(element,parent);
    }

    public boolean isComplete(){
        if(root == null)return false;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false;
        while (!queue.isEmpty()){
            Node<E> node = queue.poll();
            //要求你是叶子节点，但是你不是叶子结点
            if(leaf && !node.isLeaf())return false;
            if(node.left != null){
                queue.offer(node.left);
            }else if(node.right !=null){//node.left == null && node.right != null
                return false;
            }
            //不管你要干什么，层序遍历左右都要入队
            if(node.right != null){
                queue.offer(node.right);

            }else {
                // node.left == null &&node.right == null
                // node.left == null &&node.right == null
                //这里leaf就是要求你必须得是叶子节点说明才是完全二叉树
                leaf = true;
            }
        }
        return true;
    }
    public static class Node<E>{
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        public Node(E element,Node<E> parent){
            this.element = element;
            this.parent = parent;
        }
        public boolean isLeaf(){
            return left == null && right == null;
        }
        public boolean hasTwoChildren(){
            return left != null && right != null;
        }
        public boolean isLeftChild(){
            return parent != null && this == parent.left;
        }
        public boolean isRightChild(){
            return parent != null && this == parent.right;
        }
        public Node<E> sibling(){
            if(isLeftChild()){
                return parent.right;
            }
            if(isRightChild()){
                return parent.left;
            }
            return null;
        }


    }



}
