package com.supremind.map;


import com.supremind.map.list.AbstractList;

public class ArrayList<E> extends AbstractList<E> {

    /**
     * 所有的元素
     */
    private E[] elements;

    private static final int DEFAULT_CAPACITY = 10;


    public ArrayList(int capacity){
        capacity = (capacity < DEFAULT_CAPACITY)?DEFAULT_CAPACITY:capacity;
        elements= (E[]) new Object[capacity];
    }
    public ArrayList(){
        this(DEFAULT_CAPACITY);
    }
    /**
     * 清除所有元素
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }




    /**
     * 获取index位置的元素
     * @param index
     * @return
     */
    public E get(int index) {
        if(index < 0 ||  index >= size){
            throw new IndexOutOfBoundsException("Index:" + index + ",Size" + size);
        }
        return elements[index];
    }

    /**
     * 设置index位置的元素
     * @param index
     * @param element
     * @return 原来的元素ֵ
     */
    public E set(int index, E element) {
       rangeCheck(index);
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    /**
     * 在index位置插入一个元素
     * @param index
     * @param element
     */
    public void add(int index, E element) {
        //可以等于size
        rangeCheckForAdd(index);
        //动态扩容
        ensureCapacity(size + 1);

        for(int i = size ; i > index;i--){
            elements[i] = elements[i - 1];
        }
//        和下面是一样的
//        for(int i = size - 1; i >= index;i--){
//            elements[i + 1] = elements[i];
//        }
        elements[index] = element;
        size++;
    }

    /**
     * 保证要有capacity的容量
     * @param capacity
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        //如果原来的比传入的参数大，就没必要动了，直接return
        if(oldCapacity >= capacity) return;

        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
        System.out.println(oldCapacity+"扩容为"+newCapacity);
    }

    @Override
    public String toString() {
       StringBuilder string  = new StringBuilder();
       string.append("size=").append(size).append(",[");
        for (int i = 0; i < size; i++) {
            if(i!=0){
                string.append(",");
            }
            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }


//    @Override
//    public String toString() {
//        return "ArrayList{" +
//                "size=" + size +
//                ", elements=" + Arrays.toString(elements) +
//                '}';
//    }


    /**
     * 删除index位置的元素
     * @param index
     * @return
     */
    public E remove(int index) {
        rangeCheck(index);
        E old = elements[index];

        for (int i = index + 1; i < size  ; i++) {
                elements[i - 1] = elements[i];
        }
        elements[--size] = null;
        return old;
    }

    public void remove(E element){
        remove(indexOf(element));
    }

    /**
     * 查看元素的索引
     * @param element
     * @return
     */
    public int indexOf(E element) {
        if(element == null){
            for (int i = 0; i < size; i++) {
                if(elements[i] == null)return i;
            }
        }else{
            for (int i = 0; i < size; i++) {
                if(element.equals(elements[i]))return i;
            }
        }

        return ELEMENT_NOT_FOUND;
    }

//	public int indexOf2(E element) {
//		for (int i = 0; i < size; i++) {
//			if (valEquals(element, elements[i])) return i; // 2n
//		}
//		return ELEMENT_NOT_FOUND;
//	}
//
//	private boolean valEquals(Object v1, Object v2) {
//		return v1 == null ? v2 == null : v1.equals(v2);
//	}
}
