package com.supremind;


import com.supremind.list.AbstractList;

public class SingleLinkedList2<E> extends AbstractList<E> {

    public SingleLinkedList2(){
        first = new Node<>(null,null);
    }

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
        //这里的first是虚拟头结点
        Node<E> prev = index == 0? first : node(index - 1);
        prev.next = new Node<>(element,prev.next);


        size++;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        //这里的first是虚拟头结点
        Node<E> prev = index == 0 ? first :node(index - 1);
        Node<E> node = prev.next;
        prev.next = node.next;

        size--;
        return node.element;
    }

    @Override
    public int indexOf(E element) {
        if(element == null){
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if(node.element == null)return i;
                node = node.next;
            }
        }else{
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if(element.equals(node.element))return i;
                node = node.next;
            }
        }

        return ELEMENT_NOT_FOUND;
    }

    private Node<E> node(int index){
        rangeCheck(index);
        Node<E> node = first.next;
        for (int i = 0; i < index; i++) {
            node= node.next;
        }
        return node;
    }
    @Override
    public String toString() {
        StringBuilder string  = new StringBuilder();
        Node<E> node = first.next;
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
