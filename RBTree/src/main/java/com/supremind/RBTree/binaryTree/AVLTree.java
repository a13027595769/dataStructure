package com.supremind.RBTree.binaryTree;


import java.util.Comparator;

public class AVLTree<E> extends BBST<E> {
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

    @Override
    protected void rotate(Node<E> r, Node<E> a, Node<E> b, Node<E> c, Node<E> d, Node<E> e, Node<E> f, Node<E> g) {
        super.rotate(r, a, b, c, d, e, f, g);
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
    }

    //TODO  既然之前是在最后做的更新高度，那就重写一下这个方法，还是调用父类的，自己特有的，再这里面写。
    @Override
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);
        //更新高度
        updateHeight(grand);
        updateHeight(parent);
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


    private static class AVLNode<E> extends Node<E>{
        int height = 1;

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }
        public int balanceFactor(){
            int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
            return  leftHeight - rightHeight;
        }
        public void updateHeight(){
            int leftHeight= left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
            height = 1 + Math.max(leftHeight,rightHeight);
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
    }

}
