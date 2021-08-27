package com.supremind.map.set;

import com.supremind.map.binaryTree.BinaryTree;
import com.supremind.map.binaryTree.RBTree;

import java.util.Comparator;

public class TreeSet<E> implements Set<E> {
    private final RBTree<E> tree;
    public TreeSet(Comparator<E> comparator){
        tree = new RBTree<>(comparator);
    }
    public TreeSet(){
        this(null);
    }
    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean contains(E element) {
        return tree.contains(element);
    }

    @Override
    public void add(E element) {
        tree.add(element);
    }

    @Override
    public void remove(E element) {
        tree.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        tree.inorderTraversal(new BinaryTree.Visitor<E>() {

            @Override
            public boolean visit(E element) {
                return visitor.visit(element);
            }
        });
    }
}
