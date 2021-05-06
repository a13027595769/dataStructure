package com.supremind.map.binaryTree;


import com.supremind.map.asserta.printer.BinaryTreeInfo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree<E>  implements BinaryTreeInfo {
    protected int size;
    protected Node<E> root;
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

        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
        }
        @Override
        public String toString() {
            return element.toString();
        }
    }
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
     * 前序遍历，这个就是层序遍历的思想
     */
    public void preorder(Visitor<E> visitor){
        //如果visitor是null 或者root是null直接return就好了
        if (visitor == null || root == null) return;
        //用栈来存储
        Stack<Node<E>> stack = new Stack<>();
        //不管是哪个，都是先把根节点入队，因为你一开始能拿到的都是根节点
        stack.push(root);
        while (!stack.isEmpty()) {
            //前序，根左右，先访问根节点
            Node<E> node = stack.pop();
            // 访问node节点
            if (visitor.visit(node.element)) return;
            //访问的时候，先让右边入队，根据栈的先进后出，又是前序，右边是最后访问的，所以，让右边先入队
            //然后左边再入队
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
    }
    public void preorder2(Visitor<E> visitor) {
        if (visitor == null || root == null) return;
        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>();
        while (true) {
            //这里第一次肯定不是空，但是是循环做的，第二次可能就是了，所以idea没有提示
            if (node != null) {
                // 访问node节点，这边是访问到左边就做操作了，那个是先都放到栈里面，一起做，这个是及时行乐，
                if (visitor.visit(node.element)) return;
                // 将右子节点入栈
                if (node.right != null) {
                    stack.push(node.right);
                }
                // 向左走
                node = node.left;
            } else if (stack.isEmpty()) {
                return;
            } else {
                // 处理右边
                node = stack.pop();
            }
        }
    }

    /**
     * 中序遍历
     */
    public void inorder(Visitor<E> visitor){
        if (visitor == null || root == null) return;
        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>();
        while (true) {
            if (node != null) {
                stack.push(node);
                // 向左走，先把左边的都给入队了，不是及时行乐，也是一起做，
                node = node.left;
            } else if (stack.isEmpty()) {
                return;
            } else {
                //代码走到这里说明左边已经探索到底了，然后就该弹出来了
                node = stack.pop();
                // 弹出来进行访问node节点，因为一直往左，而且是左根右，其实某种程度上说，根据这种遍历方式
                // 左就是根
                if (visitor.visit(node.element)) return;
                // 这个时候就轮到右了，让右节点进行中序遍历
                node = node.right;
            }
        }
    }

    /**
     * 后序遍历
     */
    public void postorder(Visitor<E> visitor){
        if (visitor == null || root == null) return;
        /* 记录上一次弹出访问的节点，为什么要这样，这个和之前的不一样，左右根，
         这种遍历方式，就是先探索到底，虽然和中序很像，但是，不行，因为毕竟根节点和左字节点好歹也是
         递归的，一根线上一起添加的，但是这个，左，右了才是根，访问的时候，应该都是叶子节点了
        这里的思路是，你不是要左右根的访问么，我就按照反过来的先给你添加到栈里面，你弹出来的时候，
        就是后序遍历了，我一开始只能访问根节点，但是，我不直接添加，我先看一下，你是不是叶子节点，你
        是叶子节点我才弹出，不是叶子节点我就入栈，所以刚开始是一直入栈的，到叶子节点之后，就开始左右根的
        弹了，然后左右都弹出来了，到父节点了，按理说就该接着弹出来，但是，它不是叶子节点，这样就会造成死循环
        所以加一个prev的node，就相当于一个Flag吧，我看一下我之前的儿子访问过没有，访问过就不能访问了，不能
        死循环，就看一下，这里是或的条件，prev != null && prev.parent == top加了个这个，然后，弹出来一个
        之后，就循环赋值给prev，左右根的根来到之后，他确实不是叶子，但是，它却是他儿子的父亲，跟说废话一样
        这样就好了，然后else里面就是右左，再说一遍，根进去就是prev != null && prev.parent == top这个条件
        因为top.isLeaf()不符合条件，是或的关系，然后就弹出来了，右左相反，因为是叶子才能弹出来
        */
        Node<E> prev = null;
        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<E> top = stack.peek();
            if (top.isLeaf() || (prev != null && prev.parent == top)) {
                prev = stack.pop();
                // 访问节点
                if (visitor.visit(prev.element)) return;
            } else {
                if (top.right != null) {
                    stack.push(top.right);
                }
                if (top.left != null) {
                    stack.push(top.left);
                }
            }
        }
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



}
