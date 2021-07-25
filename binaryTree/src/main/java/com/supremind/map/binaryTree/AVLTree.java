package com.supremind.map.binaryTree;

import java.util.Comparator;

public class AVLTree<E> extends BST<E> {
    public AVLTree(){
        this(null);
    }
    public AVLTree(Comparator<E> comparator){
        super(comparator);
    }

    @Override
    protected void afterAdd(Node<E> node) {
        //这里没写错，就是把node.parent赋值给node，一直往上找，如果找到最上面为null了都不失衡，那就说明不失衡
        while((node = node.parent)!=null){
            if(isBalanced(node)){
                //更新高度
                updateHeight(node);
            }else {
                //恢复平衡，这个就是三个节点中最高的那个
                rebalance(node);
                //恢复完平衡直接break掉
                break;
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        //这里没写错，就是把node.parent赋值给node，一直往上找，如果找到最上面为null了都不失衡，那就说明不失衡
        while((node = node.parent)!=null){
            if(isBalanced(node)){
                //更新高度
                updateHeight(node);
            }else {
                //恢复平衡，这个就是三个节点中最高的那个
                rebalance(node);
                //这里不用 break，因为可能经过调整一直往上都需要调整
            }
        }

    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }
    private boolean isBalanced(Node<E> node){
        return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;
    }
    private void updateHeight(Node<E> node){
        ((AVLNode<E>) node).updateHeight();

    }

    /**
     * 恢复平衡
     * @param grand 高度最低的那个不平衡节点
     */
    private void rebalance2(Node<E> grand){
        Node<E> parent = ((AVLNode)grand).tallerChild();
        Node<E> node = ((AVLNode)parent).tallerChild();
        if(parent.isLeftChild()){//L
            if(node.isLeftChild()){ //LL
                rotateRight(grand);
            }else{//LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        }else {
            if(node.isLeftChild()){//RL
                rotateRight(parent);
                rotateLeft(grand);
            }else {//RR
                rotateLeft(grand);
            }
        }
    }
    /**
     * 恢复平衡
     * @param grand 高度最低的那个不平衡节点
     */
    private void rebalance(Node<E> grand){
        Node<E> parent = ((AVLNode)grand).tallerChild();
        Node<E> node = ((AVLNode)parent).tallerChild();
        if(parent.isLeftChild()){//L
            if(node.isLeftChild()){ //LL
                rotate(grand,node.left,node,node.right,parent,parent.right,grand,grand.right);
            }else{//LR
                rotate(grand,parent.left,parent,node.left,node,node.right,grand,grand.right);
            }
        }else {
            if(node.isLeftChild()){//RL
                rotate(grand,grand.left,grand,node.left,node,node.right,parent,parent.right);
            }else {//RR
                rotate(grand,grand.left,grand,parent.left,parent,node.left,node,node.right);
            }
        }
    }
    private void rotate(
                Node<E> r,  //子树的根节点
                Node<E> a,Node<E> b,Node<E> c,
                Node<E> d,
                Node<E> e,Node<E> f,Node<E> g){
            //先让d的parent指向根节点的parent,就是让d成为这颗子树的根节点
            d.parent = r.parent;
            if(r.isLeftChild()){
                r.parent.left = d;
            }else if(r.isRightChild()){
                r.parent.right = d;
            }else {
                root = d;
            }
            //a-b-c
            b.left = a;
            if(a != null){
                a.parent = b;
            }
            b.right = c;
            if(c != null){
                c.parent = b;
            }
            updateHeight(b);
            //e-f-g
            f.left = e;
            if(e != null){
                e.parent = f;
            }
            f.right = g;
            if(c != null){
                g.parent = f;
            }
            updateHeight(f);
            //b-d-f
            d.left = b;
            d.right = f;
            b.parent = d;
            f.parent = d;
            updateHeight(d);



    }

    private void rotateLeft(Node<E> grand){
        Node<E> parent = grand.right;
        Node<E> child = parent.left;

        grand.right = child;
        parent.left = grand;

        afterRotate(grand,parent,child);
    }
    private void rotateRight(Node<E> grand){
        Node<E> parent = grand.left;
        Node<E> child = parent.right;

        grand.left = child;
        parent.right = grand;

        afterRotate(grand,parent,child);
    }

    private void afterRotate(Node<E> grand,Node<E> parent,Node<E> child){
        //让parent成为子树的根节点
        parent.parent = grand.parent;
        if(grand.isLeftChild()){
            grand.parent.left = parent;
        }else if(grand.isRightChild()){
            grand.parent.right = parent;
        }else {  //grand是root节点
            root = parent;
        }
        //更新child的parent
        if(child != null){
            child.parent = grand;
        }
        //更新grand
        grand.parent = parent;
        //更新高度
        updateHeight(grand);
        updateHeight(parent);
    }
    private static class AVLNode<E> extends Node<E>{
        int height = 1;

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }


        public int balanceFactor(){
            //TODO Refactory is Batter?
//            Tree tree = new Tree().invoke();
            Tree tree = new Tree();
         //   int leftHeight = tree.getLeftHeight();
        //    int rightHeight = tree.getRightHeight();
            return  tree.getLeftHeight() - tree.getRightHeight();
        }

        public void updateHeight(){
            //Tree tree = new Tree().invoke();
            Tree tree = new Tree();
//            int leftHeight= left == null ? 0 : ((AVLNode<E>)left).height;
//            int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
            height = 1 + Math.max(tree.getLeftHeight(),tree.getRightHeight());
        }
        public Node<E> tallerChild(){
            int leftHeight= left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
            if(leftHeight > rightHeight) return left;
            if(leftHeight < rightHeight) return right;
            return isLeftChild()?left:right;

        }

        @Override
        public String toString() {
            String parentString = "null";
            if(parent != null){
                parentString = parent.element.toString();
            }
            return element + "_p(" + parentString + ")_h("+ height +")";
        }

        private class Tree {
            private final int leftHeight;
            private final int rightHeight;

            public int getLeftHeight() {
                return leftHeight;
            }

            public int getRightHeight() {
                return rightHeight;
            }

            public Tree() {
                leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
                rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
               // return this;
            }
        }
    }

}
