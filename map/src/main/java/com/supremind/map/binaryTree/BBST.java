package com.supremind.map.binaryTree;

import java.util.Comparator;

public class BBST<E> extends BST<E> {

    public BBST(){
        this(null);
    }
    public BBST(Comparator<E> comparator){
        super(comparator);
    }
    protected void rotateLeft(Node<E> grand){
        Node<E> parent = grand.right;
        Node<E> child = parent.left;

        grand.right = child;
        parent.left = grand;
        //调整父子关系的代码，你parent变成根节点了，那grand的父亲应该指向parent一些操作都放到这里面去做吧。
        afterRotate(grand,parent,child);
    }
    protected void rotateRight(Node<E> grand){
        Node<E> parent = grand.left;
        Node<E> child = parent.right;

        grand.left = child;
        parent.right = grand;
        //调整父子关系的代码，你parent变成根节点了，那grand的父亲应该指向parent一些操作都放到这里面去做吧。
        afterRotate(grand,parent,child);
    }

    protected void afterRotate(Node<E> grand,Node<E> parent,Node<E> child){
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
//        //更新高度,现在抽取出来AVL树和红黑树公共的了，但是更新高度只在AVL树中才有，所以应该放到AVL树中去
//        updateHeight(grand);
//        updateHeight(parent);
    }
    protected void rotate(
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
        //这些更新高度的，其实可以放在最后，低的先更新，最后再更新高的，低的两个没有父子关系，就谁先谁后都行
//        updateHeight(b);
        //e-f-g
        f.left = e;
        if(e != null){
            e.parent = f;
        }
        f.right = g;
        if(c != null){
            g.parent = f;
        }
//        updateHeight(f);
        //b-d-f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
        //上面两个是移动到下面了，然后上面俩注释了，总共就三个
//        updateHeight(b);
//        updateHeight(f);

//        updateHeight(d);



    }

}
