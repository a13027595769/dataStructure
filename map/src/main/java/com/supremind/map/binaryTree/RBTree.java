package com.supremind.map.binaryTree;


import java.util.Comparator;

public class RBTree<E> extends BBST<E> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBTree(){
        this(null);
    }
    public RBTree(Comparator<E> comparator){
        super(comparator);
    }


    @Override
    protected void afterAdd(Node<E> node) {
        //传过来的node节点就是新添加的节点，因为你要根据判断当前节点的叔父节点是不是红色啊之类的
        //也要判断父节点
        Node<E> parent = node.parent;
        //如果添加的是根节点，直接染成黑色,如果上溢到了根节点，也是把它当做新添加的节点，
        // 新添加的节点也是染成黑色的

        if(parent == null){
            black(node);
            return;
        }
        //如果父节点是黑的，那你直接return就行了，这四种情况不用考虑
        if(isBlack(parent))return;
        //uncle节点
        Node<E> uncle = parent.sibling();
        //祖父节点
       // Node<E> grand = parent.parent;
        //这里直接把一个染成红色的节点给你接收了。
        Node<E> grand = red(parent.parent);
        if(isRed(uncle)){
            black(parent);
            black(uncle);
            //把祖父节点当做是新添加的节点
//            red(grand);
////            afterAdd(grand);
            //todo  这个逻辑是要做的，之前忘了
            afterAdd(grand);
            //发现后面的都要染成红色，那还是能够抽取的。
            return;
        }
                //叔父节点不是红色
        if(parent.isLeftChild()){//L
            /*
            因为默认添加的节点都是红色的，父节点要染黑，能来到这，祖父节点都是黑的，因为就那四种情况
             */
            if(node.isLeftChild()){//LL
                black(parent);
            }else{//LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);

        }else{//R
            if(node.isLeftChild()){//RL
                black(node);
                rotateRight(parent);
            }else {//RR
                black(parent);
            }
            rotateLeft(grand);

        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        //用以取代node的子节点是红色，红黑树中，直接和颜色挂钩。
        if(isRed(node)){
            //因为要取代的节点要独立成一个节点，那么直接就染成黑色就好了
            black(node);
            return;
        }
        Node<E> parent = node.parent;
        //删除的是根节点
        if(parent == null)return;

        boolean left = parent.left == null || node.isLeftChild();
        //是左边吗？是左边，你的兄弟就是右边，否则就是左边，如果上面那行代码不删除，这边sibling可能会
        //为null，但是我们分析的情况兄弟不可能为null的。
        Node<E> sibling = left ? parent.right : parent.left;

        if(left){//被删除的节点在左边，兄弟节点在右边
            if(isRed(sibling)){//兄弟节点是红色，就是要把侄子变成兄弟了，然后进行旋转
                black(sibling);
                red(parent);
                rotateLeft(parent);
                //更换兄弟
                sibling = parent.right;
            }
            //兄弟节点必然是黑色
            if(isBlack(sibling.left) && isBlack(sibling.right)){
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if(parentBlack){
                    afterRemove(parent);
                }
            }else{
                if(isBlack(sibling.right)){
                    rotateRight(sibling);
                    sibling = parent.right;
                }
                color(sibling,colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        }else {
            if(isRed(sibling)){//兄弟节点是红色，就是要把侄子变成兄弟了，然后进行旋转
                black(sibling);
                red(parent);
                rotateRight(parent);
                //更换兄弟
                sibling = parent.left;
            }
            //兄弟节点必然是黑色
            if(isBlack(sibling.left) && isBlack(sibling.right)){
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if(parentBlack){
                    afterRemove(parent);
                }
            }else{
                if(isBlack(sibling.left)){
                    rotateLeft(sibling);
                    sibling = parent.left;
                }
                color(sibling,colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode<>(element, parent);
    }
    private Node<E> color(Node<E> node,boolean color){
        if(node != null){
            ((RBNode<E>)node).color = color;
        }
        return node;
    }
    private Node<E> red(Node<E> node){
        return color(node,RED);
    }

    private Node<E> black(Node<E> node){
        return color(node,BLACK);
    }

    private boolean colorOf(Node<E> node){
        return node == null ? BLACK : ((RBNode<E>)node).color;
    }

    private boolean isBlack(Node<E> node){
        return colorOf(node) == BLACK;
    }
    private boolean isRed(Node<E> node){
        return colorOf(node) == RED;
    }
    private static class RBNode<E> extends Node<E>{
        boolean color = RED;
        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if(color == RED){
                str = "R_";
            }
            return str + element.toString();
        }
    }

}
