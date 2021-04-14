package com.supremind.RBTree;

import com.supremind.asserta.printer.BinaryTreeInfo;

import java.io.File;
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
        /**
         * node是一直在往左或者往右走的，当往左往右走的时候为null的时候就应该插入了，
         * 但是node已经变了，parent就是记录之前没有变时候的值，作为新建节点的父节点
         */
        //添加的不是第一个节点
        //找到父节点
        Node<E> parent = root;
        //这个node一直在变化，就是判断比element大还是小，找左右子节点，如果小往左边走，大了右边走，直到为null就添加
        Node<E> node = root;
        int cmp = 0;
        while (node != null){
            cmp = compare(element,node.element);
            //下一步只有为null的时候才可能跳出循环，就是把left或者right赋值给node的时候，那么没有赋值之前，就是parent
            //因为如果为循环过后，左节点或者右节点为null就该跳出循环了，但是之前的node就会丢失，
            //所以让parent来存一下，下面创建对象的时候能用到
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
        //当上面一直遍历，往左往右走的时候为null了，就可以插了，parent就是node没有变之前的。
        Node<E> newNode = new Node<>(element,parent);
        if(cmp > 0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }
        size++;

    }
    public void remove(E element){
        remove(node(element));
    }

    private void remove(Node<E> node){
        if(node == null)return;
        size--;

        if(node.hasTwoChildren()){
            //找到后继节点
            Node<E> s = successor(node);
            //用后继节点的值覆盖度为2的节点
            //为什么要加这行代码，不加下面不还会把s赋值给了node吗？这不是脱裤子放屁么，其实不是，如果你不加
            //node.element没有变化，还是原来的值，就是前驱节点没有覆盖要删除节点，下面是改变了一下前驱节点的
            //指向，而没有赋值，所以，这一行代码，必须加！！

            node.element = s.element;
            //删除的其实是继节点
            //因为后面要删除node，这里要删除s，所以就把s赋值给node，后面统一处理就可以了
            node = s;
        }
        //删除node节点（node节点的度必然是1或者0）
        Node<E> replacement = node.left != null? node.left:node.right;

        if(replacement != null){//node是度为1的节点
            //更改parent
            replacement.parent = node.parent;
            //更改parent的left、right的指向
            if(node.parent == null){//node是度为1并且是根节点，但是下面还有子节点，就需要将root指针指向子节点
                root = replacement;
            }else if(node == node.parent.left){
                node.parent.left = replacement;
            }else {//node == node.parent.right
                node.parent.right = replacement;
            }
        }else if(node.parent == null){//node是叶子结点,并且是根节点
            root = null;
        }else {//node是叶子结点,但不是根节点
            if(node == node.parent.left){
                node.parent.left = null;
            }else {
                node.parent.right = null;
            }
        }

    }

    private Node<E> node(E element){
        Node<E> node = root;
        while (node != null){
            int cmp = compare(element,node.element);
            if(cmp == 0)return node;
            if(cmp > 0){
                node = node.right;
            }else {
                node = node.left;
            }
        }
        return null;
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
        postorderTraversal(node.left,visitor);
        postorderTraversal(node.right,visitor);
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
    //前驱结点
    private Node<E> predecessor(Node<E> node){
        if(node == null) return null;
        //前驱节点在左子树当中(left.right.right...)
        //前驱节点肯经比当前节点小啊，必定在左子树，这是毋庸置疑的，但是前驱结点又是比它小的节点里面最大的，
        //就得一直right了
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
        //能来到最后退出循环，无外乎两种情况，
        //1.父节点为null了，那就是没有前驱结点， node.parent == null
//      //2.不是父节点的左节点而是右节点 node.parent.right
//             ┌───7_p(null)───┐
//             │               │
//        ┌─4_p(7)─┐       ┌─9_p(7)─┐
//        │        │       │        │
//   ┌─2_p(4)─┐  5_p(4) 8_p(9) ┌─11_p(9)─┐
//   │        │                │         │
//  1_p(2)    3_p(2)         10_p(11)   12_p(11)
    //例如8的前驱节点就是7
        //我们不用去判断如果是null返回啥，如果不是null又返回啥，不用ifelse了，直接返回，有就是有，没有就是没有
        return node.parent;
    }

    //后继节点
    private Node<E> successor(Node<E> node){
        if(node == null) return null;
        //后继节点在右子树当中(right.left.left...)
        Node<E> p = node.right;
        if(p != null){
            while (p.left !=null){
                p = p.left;
            }
            return p;
        }
        //程序来到这里，说明右子树为null，要从祖父节点查找
        while(node.parent != null && node == node.parent.right){
            node = node.parent;
        }
        //1.父节点为null了，那就是没有前驱结点， node.parent == null
        //2.不是父节点的左节点而是右节点 node.parent.left
//             ┌───7_p(null)───┐
//             │               │
//        ┌─4_p(7)─┐       ┌─9_p(7)─┐
//        │        │       │        │
//   ┌─2_p(4)─┐  5_p(4) 8_p(9) ┌─11_p(9)─┐
//   │        │                │         │
//  1_p(2)    3_p(2)         10_p(11)   12_p(11)
//8的后继节点就是9
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
                //每当当前这一层元素都被访问完才能进入这个if判断，并且把下一层的元素个数赋值给levelSize
                //不用担心会不会乱了，因为每一次是把下一层的复制之后，levelsize不减到0是不会进行重新赋值的
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
                // node.left != null &&node.right == null
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
        File file = new File("c:");
        return ((Comparable<E>)e1).compareTo(e2);
    }
    public boolean contains(E element){
        return node(element) != null;
    }

    private void elementNotNullCheck(E element){
        if(element == null){
            throw new IllegalArgumentException("element must not be null");
        }
    }

}
