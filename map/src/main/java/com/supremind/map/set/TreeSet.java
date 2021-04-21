package com.supremind.map.set;

import com.supremind.map.Map;
import com.supremind.map.TreeMap;
import com.supremind.map.binaryTree.BinaryTree;
import com.supremind.map.binaryTree.RBTree;

import java.util.Comparator;

public class TreeSet<E> implements Set<E> {
    private Map<E,Object> map = new TreeMap<>();
    private static final Object PRESENT = new Object();
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(E element) {
        return map.containsKey(element);
    }

    @Override
    public void add(E element) {
        map.put(element,PRESENT);
    }

    @Override
    public void remove(E element) {
        map.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        map.traversal(new Map.Visitor<E, Object>() {

            @Override
            public boolean visit(E key, Object value) {
                return visitor.visit(key);
            }
        });
    }
}
