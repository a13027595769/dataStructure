package com.supremind.map.treemap;

import java.util.*;

public class TestMap<K, V> implements Map<K, V> {
    private int size;
    private Node<K,V> root;
    private final Comparator<K> comparator;
    public TestMap(Comparator<K> comparator){
        this.comparator = comparator;
    }
    public TestMap(){
        this(null);
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = node((K) key);
        return node != null ? node.value : null;
    }

    @Override
    public V put(K key, V value) {
        keyNotNullCheck(key);
        //添加第一个节点
        if(root == null){
            root = new Node<>(key,value,null);
            size++;
            return null;
        }
        //添加的不是第一个节点
        //找到父节点
        Node<K,V> parent = root;
        //这个node一直在变化，就是判断比element大还是小，找左右子节点，如果小往左边走，大了右边走，直到为null就添加
        Node<K,V> node = root;
        int cmp = 0;
        while (node != null){
            cmp = compare(key,node.key);
            //下一步只有为null的时候才可能跳出循环，就是把left或者right赋值给node的时候，那么没有赋值之前，就是parent
            parent = node;
            if(cmp > 0){
                node = node.right;
            }else if(cmp < 0){
                node = node.left;
            }else {//相等
                node.key = key;
                //这里和之前不一样了，以前是只有一个元素，现在是k,v要两个都覆盖一下
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }

        //看看父节点插入到哪个位置
        Node<K,V> newNode =  new Node<>(key,value,parent);
        if(cmp > 0){
            parent.right = newNode;
        }else {
            parent.left = newNode;
        }
        size++;

        return null;
    }

    @Override
    public V remove(Object key) {
        return remove(node((K) key));
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    private void keyNotNullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
    }

    private int compare(K e1,K e2){
        if(comparator != null){
            return comparator.compare(e1,e2);
        }
        return ((Comparable<K>)e1).compareTo(e2);
    }
    private static class Node<K,V>{
        K key;
        V value;
        Node<K,V> left;
        Node<K,V> right;
        Node<K,V> parent;
        public Node(K key,V value,Node<K,V> parent){
            this.key = key;
            this.value = value;
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
        public Node<K,V> sibling(){
            if(isLeftChild()){
                return parent.right;
            }
            if(isRightChild()){
                return parent.left;
            }
            return null;
        }
    }

    private Node<K,V> node(K key){
        Node<K,V> node = root;
        while (node != null){
            int cmp = compare(key,node.key);
            if(cmp == 0)return node;
            if(cmp > 0){
                node = node.right;
            }else {
                node = node.left;
            }
        }
        return null;
    }
    private V remove(Node<K,V> node){
        if(node == null)return null;
        size--;
        V oldValue = node.value;
        if(node.hasTwoChildren()){
            //找到后继节点
            Node<K,V> s = successor(node);
            //用后继节点的值覆盖度为2的节点
            node.key = s.key;
            node.value = s.value;
            //删除的其实是后继节点
            //因为后面要删除node，这里要删除s，所以就把s赋值给node，后面统一处理就可以了
            node = s;
        }
        //删除node节点（node节点的度必然是1或者0）
        Node<K,V> replacement = node.left != null? node.left:node.right;

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
            //删除节点之后的处理
        }else {//node是叶子结点,但不是根节点
            if(node == node.parent.left){
                node.parent.left = null;
            }else {
                node.parent.right = null;
            }
        }
        return oldValue;
    }

    //后继节点
    protected Node<K,V> successor(Node<K,V> node){
        if(node == null) return null;
        //后继节点在右子树当中(right.left.left...)
        Node<K,V> p = node.right;
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
        return node.parent;
    }
    public static void main(String[] args) {
        Map<String,Integer> map = new TestMap<>();
        map.put("c",2);
        map.put("a",5);
        System.out.println(map.get("a"));
        map.put("b",6);
        map.put("a",8);
        System.out.println(map.get("c"));
        System.out.println(map.get("a"));
        System.out.println(map.remove("b"));
        System.out.println(map.size());
    }
}