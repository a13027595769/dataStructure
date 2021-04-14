package com.supremind.RBTree.binaryTree;


import java.util.Comparator;

public class RBTreeErrorBAK<E> extends BBST<E> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBTreeErrorBAK(){
        this(null);
    }
    public RBTreeErrorBAK(Comparator<E> comparator){
        super(comparator);
    }
    private Node<E> color(Node<E> node,boolean color){
//        if(node == null) return node;
//        ((RBNode<E>)node).color = color;
//        return node;
//        TODO
          if(node != null){
              ((RBNode<E>)node).color = color;
          }
          return node;
    }

    @Override
    protected void afterAdd(Node<E> node) {
        //传过来的node节点就是新添加的节点，因为你要根据判断当前节点的叔父节点是不是红色啊之类的
        //也要判断父节点
        Node<E> parent = node.parent;
        //如果添加的是根节点，直接染成黑色,如果上溢到了根节点，也是把它当做新添加的节点，新添加的节点也是染成黑色的

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
//            afterAdd(red(grand));
            //发现后面的都要染成红色，那还是能够抽取的。
            return;
        }
//        //叔父节点不是红色
//        if(parent.isLeftChild()){//L
//            if(node.isLeftChild()){//LL
//                black(parent);
//                red(grand);
//                rotateRight(grand);
//            }else{//LR
//                black(node);
//                red(grand);
//                rotateLeft(parent);
//                rotateRight(grand);
//            }
//        }else{//R
//            if(node.isLeftChild()){//RL
//                black(node);
//                red(grand);
//                rotateRight(parent);
//                rotateLeft(grand);
//            }else {//RR
//                black(parent);
//                red(grand);
//                rotateLeft(grand);
//            }
//        }
                //叔父节点不是红色
        if(parent.isLeftChild()){//L
            red(grand);

            if(node.isLeftChild()){//LL
                black(parent);
            }else{//LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);

        }else{//R
            red(grand);
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
        //如果删除的是RED节点，直接删除就好了
        if(isRed(node)) return;

        //用以取代node的子节点是红色，红黑树中，直接和颜色挂钩，不要去想度为1还有2了，跳出来吧。
        if(isRed(node)){
            //因为要取代的节点要独立成一个节点，那么直接就染成黑色就好了
            black(node);
            return;
        }
        Node<E> parent = node.parent;
        //删除的是根节点
        if(parent == null)return;

        //删除的是黑色叶子结点
        /*
         因为刚才讲的都是看删除节点的兄弟是不是黑色啊，有没有红色子节点啊，这之类的，所以我们下意识就会写
         这样的代码
         Node<E> sibling = node.sibling();
         首先想一下，站在二叉树的角度想一下，能来到这里的，肯定是二叉树里面的叶子节点，
         当时是叶子节点，直接就删除了

         但是，我们要看一下，它是怎么做的：
          public Node<E> sibling(){
            if(isLeftChild()){
                return parent.right;
            }
            if(isRightChild()){
                return parent.left;
            }
            return null;
        }
        public boolean isLeftChild(){
            return parent != null && this == parent.left;
        }
        既然是叶子节点，那么它必定是走过了这个阶段，那说明左边右边这根线已经给断了。
           else {//node是叶子结点,但不是根节点
            if(node == node.parent.left){
                node.parent.left = null;
            }else {
                node.parent.right = null;
            }
            //删除节点之后的处理
            afterRemove(node,null);
        }
        走过这里就说明已经被清空了，然后呢，我们可以判断谁是空的，不就知道到底是谁被删除了吗？你是
        左，那你的兄弟就是右，你是右，你的兄弟就是左，妙啊！
        所以，现在node已经是空的了，下面那个必定会报空指针异常
         */
        boolean left = parent.left == null || node.isLeftChild();
        //是左边吗？是左边，你的兄弟就是右边，否则就是左边
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
                //旋转过后，兄弟节点要继承原来父节点的颜色的，可以先染色，也可以先旋转，这里先染色
                //这里不直接说染黑色还是白色，我们去查查父节点啥颜色，再赋值给兄弟
                color(sibling,colorOf(parent));
                //然后，兄弟的左边和父节点都要染成黑色，不是必须红黑红了，黑黑黑也行，红黑树不是必须
                // 黑红交替的，节点小的话，全黑也行。
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
                //如果进来这里，说明兄弟节点的左跟右都是黑色的，如果是null呢？那你就当是假想出来的黑节点
                //还是这样，假想出来的默认都是黑色的啊，然后你删除，就下溢了，下溢了就要跟父节点合并，父结点
                //变色，兄弟也变色，，父节点变成黑色，兄弟节点变成红色
                //先判断父节点是不是黑色的，如果是黑色的必然要产生下溢的，下溢就再递归调用，直接删掉
                //怎么删呢？递归调用
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if(parentBlack){
                    //就跟添加上溢一样，上溢到根节点，上面就又会判断一下，是不是空啊。

                    afterRemove(parent);
                    /*
                     //删除的是根节点
                     if(parent == null)return;
                    又会走到这里。
                     */
                }

            }else{//兄弟节点至少有一个红色子节点，像兄弟借，然后就是那三种情况了，兄弟左边是红的，
                //兄弟右边是红的，兄弟左右都是红的，  左边是红的，和两边都是红的，就是LL情况，直接
                //右旋转，如果右边是红的呢？先左旋，再又旋，那么直接处理这种，我先左旋了，最终都要
                //右旋转，直接处理一次，就跟删除度为2的节点一样，找前驱或者后继，都是要删除的，我
                //就先赋值一下，最后统一处理，也挺骚的。。，但是，终究还是有问题，因为你左旋之后，关系没有
                //处理好。左旋之后，就是往下面走了，那就不是你的兄弟了，就变成了你的侄子了。怎么处理呢？
                //父节点的左边再重新赋值给兄弟，就好了。sibling = parent.left;这句代码
                //先判断兄弟左边是不是黑的，没有就默认是null,null默认就是黑的。
                if(isBlack(sibling.left)){
                    rotateLeft(sibling);
                    sibling = parent.left;
                }
                //旋转过后，兄弟节点要继承原来父节点的颜色的，可以先染色，也可以先旋转，这里先染色
                //这里不直接说染黑色还是白色，我们去查查父节点啥颜色，再赋值给兄弟
                color(sibling,colorOf(parent));
                //然后，兄弟的左边和父节点都要染成黑色，不是必须红黑红了，黑黑黑也行，红黑树不是必须
                // 黑红交替的，节点小的话，全黑也行。
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
