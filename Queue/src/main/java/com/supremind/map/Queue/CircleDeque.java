package com.supremind.map.Queue;

public class CircleDeque<E> {
    private int front;
    private int size;
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;
    public CircleDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }
    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    /**
     *
     * @param element
     */
    public void enQueueRear(E element){
        ensureCapacity(size + 1);
        elements[index(size++)] = element;
    }
    public E deQueueFront(){
        E frontElement = elements[front];
        elements[front] = null;
        //原来是front++,但是如果到最右边，再++可能就会超出下标，所以还是要取模，就直接加1了，没有++
        front = index(1);
        size--;
        return frontElement;
    }
    public void enQueueFront(E element){
        ensureCapacity(size + 1);
        //往前面加，front指向的元素也是要变化的，所以先让front改变，再去front真正的地址去设置值
        front = index(-1);
        elements[front] = element;
        size++;
    }
    public E deQueueRear(){
        int rearIndex = index(size - 1);
        E rear = elements[rearIndex];
        elements[rearIndex] = null;
        size--;
        return rear;
    }

    public E front(){
        return elements[front];
    }
    public E rear(){
        return elements[index(size - 1)];
    }
    public void clear(){

        for (int i = 0; i < size; i++) {
            elements[index(i)] = null;
        }
        size = 0;
        front = 0;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("capcacity=").append(elements.length)
                .append(" size=").append(size)
                .append(" front=").append(front)
                .append(", [");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }
    private int index(int index){
        index += front;
        if(index < 0){
            return index + elements.length;
        }
       // return index % elements.length;
        return index - (index >= elements.length ? elements.length : 0);
    }

    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        //如果原来的比传入的参数大，就没必要动了，直接return
        if(oldCapacity >= capacity) return;

        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            //front再加上i获取真实的地址
            newElements[i] = elements[index(i)];
        }
        elements = newElements;
        //重置front
        front = 0;


    }
}
