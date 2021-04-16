package com.supremind.map.Queue;


import java.util.LinkedList;
import java.util.List;

public class Queue<E> {
    private List<E> list = new LinkedList<>();

    @Override
    public String toString() {
        return "Queue{" +
                "list=" + list +
                '}';
    }

    public int size(){
        return list.size();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

    public void enQueue(E element){
        list.add(element);
    }

    public E deQueue(){
        return list.remove(0);
    }

    public E front(){
        return list.get(0);
    }
}
