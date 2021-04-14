package com.supremind.二叉树;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<E> implements BinaryTreeInfo {
    private int size;
    private Node<E> root;
    private Comparator<E> comparator;

    public BinarySearchTree(Comparator<E> comparator){
        this.comparator = comparator;
    }
    public BinarySearchTree(){
        this(null);
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

    @Override
    public Object string(Object node) {
        Node<E> myNode = (Node<E>) node;
        String parentString = "null";
        if(myNode.parent != null){
            parentString = myNode.parent.element.toString();
        }
        return myNode.element + "_p(" + parentString + ")";
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
    }
    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }
    public void clear(){

    }
    public void add(E element){
        elementNotNullCheck(element);
        //添加第一个节点
        if(root == null){
            root = new Node<>(element,null);
            size++;
            return;
        }
        //添加的不是第一个节点
        //找到父节点
        Node<E> parent = root;
        //这个node一直在变化，就是判断比element大还是小，找左右子节点，如果小往左边走，大了右边走，直到为null就天剑
        Node<E> node = root;
        int cmp = 0;
        while (node != null){
            cmp = compare(element,node.element);
            //下一步只有为null的时候才可能跳出循环，就是把left或者right赋值给node的时候，那么没有赋值之前，就是parent
            parent = node;
            if(cmp > 0){
                node = node.right;
            }else if(cmp < 0){
                node = node.left;
            }else {//相等
                node.element = element;
                return;
            }
        }

        //看看父节点插入到哪个位置
        Node<E> newNode = new Node<>(element,parent);
        if(cmp > 0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }
        size++;

    }
    public void remove(E element){

    }
    //前序遍历
    public Node<E> inverseTree1(Node root){
        if(root == null) return root;

        Node tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        inverseTree1(root.left);
        inverseTree1(root.right);
        return root;
    }
    //后序遍历
    public Node<E> inverseTree2(Node root){
        if(root == null) return root;


        inverseTree2(root.left);
        inverseTree2(root.right);
        Node tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        return root;
    }
    //中序遍历
    public Node<E> inverseTree3(Node root){
        if(root == null) return root;


        inverseTree3(root.left);
        Node tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        inverseTree3(root.left);
        return root;
    }
    //层序遍历
    public Node<E> inverseTree4(Node root){
        if(root == null) return root;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()){
            Node<E> node = queue.poll();
            // System.out.println(node.element);
            Node tmp = node.left;
            node.left = node.right;
            node.right = tmp;
            if(node.left != null){
                queue.offer(node.left);
            }
            if(node.right != null){
                queue.offer(node.right);
            }
        }
        return root;
    }
    /**
     * 前序遍历
     */
    public void preorderTraversal(Visitor<E> visitor){
        if(visitor == null)return;
        preorderTraversal(root,visitor);
    }
    private void preorderTraversal(Node<E> node,Visitor<E> visitor){
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
    private void inorderTraversal(Node<E> node,Visitor<E> visitor){
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
    private void postorderTraversal(Node<E> node,Visitor<E> visitor){
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

    /**
     * 层序遍历
     */
//    public void levelOrderTraversal(){
//        if(root == null)return;
//        Queue<Node<E>> queue = new LinkedList<>();
//        queue.offer(root);
//        while (!queue.isEmpty()){
//            Node<E> node = queue.poll();
//            System.out.println(node.element);
//            if(node.left != null){
//                queue.offer(node.left);
//            }
//            if(node.right != null){
//                queue.offer(node.right);
//            }
//        }
//    }
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


    public static abstract class Visitor<E> {
        boolean stop;
        abstract boolean visit(E element);
    }
    /**
     * @return  返回值等于0代表e1和e2相等,返回值大于0代表e1大于e2,返回值小于0,代表e1小于e2
     */
    private int compare(E e1,E e2){
        if(comparator != null){
            return comparator.compare(e1,e2);
        }
        return ((Comparable<E>)e1).compareTo(e2);
    }
    public boolean contains(E element){
        return false;
    }

    private void elementNotNullCheck(E element){
        if(element == null){
            throw new IllegalArgumentException("element must not be null");
        }
    }
}
