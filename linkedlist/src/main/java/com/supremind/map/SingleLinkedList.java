package com.supremind.map;


import com.supremind.map.list.AbstractList;

public class SingleLinkedList<E> extends AbstractList<E> {
    private int size;

    private Node<E> first;

    private static class Node<E>{

        E element;
        Node<E> next;
                    //当前元素     下一个节点
        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
    }

    @Override
    public E get(int index) {
        return node(index).element;
    }

    @Override
    public E set(int index, E element) {
        //先根据索引查到当前节点，把当前的元素先用变量接收，再把传入过来的元素赋值给当前元素，再把原来的元素返回
        Node<E> node = node(index);
        E old = node.element;
        node.element = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        if(index == 0){
            //添加到最前面，就相当于超过第一，他就是第二，new的一个Node的下一个节点就是原来的first
            //再把这个值赋值给原来的first，这才加名正言顺
            first = new Node<>(element,first);

        }else {
            Node<E> prev = node(index - 1);
            prev.next = new Node<>(element,prev.next);
        }

        size++;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        Node<E> node = first;
        if(index == 0){
            first = first.next;
        }else{
            Node<E> prev = node(index - 1);
            node = prev.next;
            prev.next = node.next;
        }
        size--;
        return node.element;
    }

    @Override
    public int indexOf(E element) {
        //这里就是说看你设计的数组或者链链表里面允不允许添加null元素，这个就要分开比较了
        //如果你java里面null.method()会报空指针异常，这个时候，我们可以找，到底哪个节点
        //里面存储了空元素，所以比较的时候,null在后面
        if(element == null){
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if(node.element == null)return i;
                node = node.next;
            }
        }else{
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                //能来到else，说明element绝对不为空，就可以把element放在前面了
                if(element.equals(node.element))return i;
                node = node.next;
            }
        }

        return ELEMENT_NOT_FOUND;
    }

    private Node<E> node(int index){
        rangeCheck(index);
        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node= node.next;
        }
        return node;
    }
    @Override
    public String toString() {
        StringBuilder string  = new StringBuilder();
        Node<E> node = first;
        string.append("size=").append(size).append(",[");
        for (int i = 0; i < size; i++) {
            if(i!=0){
                string.append(",");
            }
            string.append(node.element);
            node = node.next;
        }
        string.append("]");
        return string.toString();
    }
}
